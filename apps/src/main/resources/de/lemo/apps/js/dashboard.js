/**
 * THIS SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * BY eksith http://eksith.wordpress.com/2012/01/02/jquery-ui-dashboard/
 * 
 */

(function($) {
	
	// Initialize
	InitDashboard();
	alert("Hello!");
	
	
	
	/**********************************
		Settings and CSS junk
	***********************************/
	
	$('head').append('<style type="text/css">' +
		'.icon { width:75px; height:75px; }' +
		'.ticon { float:left; margin: .3em .5em; width:25px; height:25px; box-shadow:1px 1px 1px #aaa; }' +
		'.ui-dialog { text-align:left; }'+
		'.ui-dialog input[type="text"], .ui-dialog textarea { display:block; width:90%; }' +
		'</style>');
	
	
	
	
	
	
	
	/**********************************
		Dashboard initialization
	***********************************/
	
	function InitDashboard() {
		
		// Initialize dialogs
		initDialogs();
		
		// Portlet and sort related CSS classes
		var sortClasses = "ui-widget ui-widget-content ui-helper-clearfix";
		
		// Set every column segment with h5 element as a sortable widget
		$('.column:has(h5)').each(function() {
			
			var s = $(this);
			var p = s.parentsUntil('section').parent();
			var h = s.children('h5:first').eq(0);
			
			if(!p.hasClass('ui-widget'))
				p.addClass(sortClasses);
			
			// Function icons
			h.addClass('ui-widget-header')
				.prepend('<span class="ui-icon ui-icon-gear" title="Config"></span>')
				.prepend('<span class="ui-icon ui-icon-minus" title="Toggle"></span>')
				.prepend('<span class="ui-icon ui-icon-close" title="Close"></span>');
	
			// Need this to drag not highlight
			h.disableSelection();
			
			// Interaction cues
			h.css('cursor', 'move');
			$('.ui-icon').css('cursor', 'pointer');
	
			// Wrap control stuff (like icons and headers) in a widget-header div
			// and the rest in a widget-content div
			s.children().not('img[alt="icon"], .ui-widget-header, .ui-icon')
				.wrapAll('<div class="widget-content" />');
			
			s.children().not('widget-content').wrapAll('<div class="widget-header" />');
		});
		
		// Group sortable widgets in each section to one sort-area
		$('section').has('.column').each(function() {
			$(this).children().not('header,hr')
				.wrapAll('<div class="sort-area" />');
		});
		
		
		// Trigger control initialization
		setControls();
	}

	
	
	
	
	/**********************************
		Dialog boxes
	***********************************/
	
	function initDialogs() {
		// Close widget dialog
		$('article').append('<div id="dialog-confirm-close-widget" title="Close widget" style="display:none;">' + 
			'<span class="ui-icon ui-icon-alert" style="float:left;"></span>' +
			'You are about to delete this widget. Are you sure?</div>');
		
		// Config widget dialog
		$('article').append('<div id="dialog-config-widget" title="Modify" style="display:none;">' +
			'<form><fieldset><legend>Change widget content</legend>' +
			'<p><label>Title <input type="text" id="widget-title-text" /></label></p>'+ 
			'<p><label>Content <textarea rows="5" cols="50" id="widget-content-text">Some test content</textarea></label></p>'+ 
			'<p id="icon-field"><label>Icon URL <span>Will be resized to 75x75 pixels</span><input type="text" id="widget-icon-text" /></label></p>'+ 
			'</fieldset></form></div>');
	
		// Create and destroy these dialogs to hide them
		$('#dialog-confirm-close-widget').dialog("destroy");
		$('#dialog-config-widget').dialog("destroy");
	}
	
	
	
	
	
	/**********************************
		Widget controls
	***********************************/
	
	// Control icons
	function setControls(ui) {
		ui = (ui)? ui : $('.ui-icon');
		ui.click(function() {
			var b = $(this);
			var p = b.parentsUntil('.column').parent();
			var i = p.children('img[alt="icon"]:first').eq(0);

			var h = p.children('.ui-widget-header h5:first').eq(0);
		
			// Control functionality
			switch(b.attr('title').toLowerCase()) {
				case 'config':
					widgetConfig(b, p);
					break;
				
				case 'toggle':
					widgetToggle(b, p, i);
					break;
				
				case 'close':
					widgetClose(b, p);
					break;
			}
		});
	}
	
	// Toggle widget
	function widgetToggle(b, p, i) {
		// Change the + into - and visa versa
		b.toggleClass('ui-icon-minus').toggleClass('ui-icon-plus');
		
		// Turn the big icon into a small one or visa versa
		if(i.hasClass('icon'))
			i.switchClass('icon', 'ticon', '300');
		else
			i.switchClass('ticon', 'icon', '300');
		
		// Show/Hide widget content
		p.children('.widget-content').eq(0).toggle(300);
	}
	
	// Modify widget
	function widgetConfig(w, p) {
		
		// Input elements in the dialog
		var dt = $('#widget-title-text');
		var dc = $('#widget-content-text');
		var du = $('#widget-icon-text');
		
		// Widget elements to change
		var wt = p.children('h5:first').eq(0);
		var wc = p.children('.widget-content').eq(0);
		
		// If there is no icon on the widget, there's nothing to change
		var wi = p.children('img[alt="icon"]:first');
		if(wi.length > 0) {
			wi = p.children('img[alt="icon"]:first').eq(0);
			$('#icon-field').show();
		}
		else {
			$('#icon-field').hide();
		}
		
		$("#dialog-config-widget").dialog({
			resizable: false,
			modal: true,
			width: 500,
			open: function() {
				if(wi != null)
					du.val(wi.attr('src'));
				
				dt.val(wt.text());
				dc.val(wc.html());
				
				// Initialize TinyMCE
				//initTiny(dc, tinySettings);
			},
			buttons: {
				"Save changes": function(e, ui) {
					
					// Some widgets don't have an icon
					if(wi.length > 0) {
						if(notEmpty(du.val()))
							wi.attr('src', du.val());
					}
					
					// Remove editor (also gets content from TinyMCE back to textarea)
					//closeTiny(dc);
					
					// Update
					if(notEmpty(dc.val()))
						wc.html(dc.val());
					
					// Careful here, don't wanna lose the control icons
					if(notEmpty(dt.val())) {
						var ci = wt.children('span.ui-icon');
						wt.html(dt.val());
						
						// Reset controls
						wt.prepend(ci);
						setControls(ci);
					}
					
					$(this).dialog("close");
				},
				Cancel: function() {
					
					// Destroy TinyMCE
					//closeTiny(dc);
					
					$(this).dialog("close");
				}
			}
		});
	}
	
	// Close widget with dialog
	function widgetClose(w, p) {
		$("#dialog-confirm-close-widget").dialog({
			resizable: false,
			modal: true,
			buttons: {
				"Close widget": function() {
					p.toggle('slide', {}, 500, function() {
						p.remove();
					});
					$(this).dialog("close");
				},
				Cancel: function() {
					$(this).dialog("close");
				}
			}
		});
	}
	
	
	
	
	
	/**********************************
		Sort functionality 
	***********************************/
	
	$(".sort-area").sortable({
		connectWith: ".sort-area",
		opacity: 0.6,
		helper: 'clone',
		dropOnEmpty: true
	});
	
	
	
	
	
	/**********************************
		Helpers
	***********************************/

	function notEmpty(t) {
		if(t) {
			if($.trim(t) != "")
				return true;
		}
		return false
	}
}) (jQuery);


