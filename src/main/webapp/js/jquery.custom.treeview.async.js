/*
 * Async Treeview 0.1 - Lazy-loading extension for Treeview
 *
 * http://bassistance.de/jquery-plugins/jquery-plugin-treeview/
 *
 * Copyright Jörn Zaefferer
 * Released under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 */

;(function($) {

function load(settings, root, child, container) {
	function createNode(parent) {
		var aTag = "<span id='tree_name'>";
		if(this.id){
			aTag ="<a class='treeview-text' style='cursor:pointer'  id='tree_" + this.id + "'" +
					" data-id='" + this.id + "' data-type='" + this.type + "'>" + aTag;
			if(!this.use_yn) aTag +="<strike>"+ this.text + "</strike></span></a>"
			else aTag += this.text + "</span></a>";
		} else aTag += this.text + "</span>";
		
		var current = $("<li/>").attr("id", this.id || "").html("<span id='icon'>&nbsp;</span>"+aTag).appendTo(parent);
		if (this.classes) {
			if(this.type=="D") this.classes = "folder";
			else if(this.type=="C") this.classes = "file";
			current.children("span#icon").addClass(this.classes);
		}
		if (this.expanded) {
			current.addClass("open");
		}
		if (this.compulsoryIcon || this.hasChildren || this.children && this.children.length) {
			var branch = $("<ul/>").appendTo(current);
			if (this.compulsoryIcon || this.hasChildren) {
				if(this.hasChildren){
					current.addClass("hasChildren");
					createNode.call({
						classes: "placeholder",
						text: "&nbsp;",
						children:[]
					}, branch);
				}else{
					createNode.call({
						classes: "",
						text: "데이터 미존재",
						children:[]
					}, branch);
				}
			}
			if (this.children && this.children.length) {
				$.each(this.children, createNode, [branch])
			}
		}
	}
	$.ajax($.extend(true, {
		url: settings.url,
		dataType: "json",
		data: {
			root: root
		},
		success: function(response) {
			child.empty();
			$.each(response, createNode, [child]);
	        $(container).custom_treeview({add: child});
	    }
	}, settings.ajax));
}

var proxied = $.fn.custom_treeview;
$.fn.custom_treeview = function(settings) {
	if (!settings.url) {
		return proxied.apply(this, arguments);
	}
	if (!settings.root) {
		settings.root = "source";
	}
	var container = this;
	if (!container.children().size())
		load(settings, settings.root, this, container);
	var userToggle = settings.toggle;
	return proxied.call(this, $.extend({}, settings, {
		collapsed: true,
		toggle: function() {
			var $this = $(this);
			if ($this.hasClass("hasChildren")) {
				var childList = $this.removeClass("hasChildren").find("ul");
				load(settings, this.id, childList, container);
			}
			if (userToggle) {
				userToggle.apply(this, arguments);
			}
		}
	}));
};

})(jQuery);
