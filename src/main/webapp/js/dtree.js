
/*--------------------------------------------------|
| dTree 2.05 | www.destroydrop.com/javascript/tree/ |
|---------------------------------------------------|
| Copyright (c) 2002-2003 Geir Landrö               |
|                                                   |
| This script can be used freely as long as all     |
| copyright messages are intact.                    |
|                                                   |
| Updated: 17.04.2003                               |
|--------------------------------------------------*/

	/******************************
	 * 이미지 디렉토리 경로
	 ******************************/
	var treeMenuImgPath	= '/smartux_adm/images/menu/';

	/****************************
	 * Node object
	 * id 		: 	내코드
	 * pid		:	상위 코드
	 * name		:	이름
	 * url		:	링크 또는 실행 함수
	 * title	:	물풍선
	 * target	:	
	 * icon		:	icon을 직접 선택
	 * iconOpen	:	iconOpen 유무 선택
	 * open		:	트리의 오픈 유무
	 * menuDiv	:	폴더('01')인지 파일('02')인지 구분
	 ****************************/
	function Node(id, pid, name, url, title, target, icon, iconOpen, open, menuDiv) {
		this.id			= id;
		this.pid		= pid;
		this.name		= name;
		this.url		= url;
		this.title		= title;
		this.target		= target;
		this.icon		= icon;
		this.iconOpen	= iconOpen;
		this._io		= open || false;
		this._is		= false;
		this._ls		= false;
		this._hc		= false;
		this._ai		= 0;
		this._p;
		this.menuDiv   = menuDiv;
	}
	/***********************
	 * Tree object
	 ***********************/
	function dTree(objName) {
		this.config = {
			target				: null
			, folderLinks		: true
			, useSelection		: true
			, useCookies		: true
			, useLines			: true
			, useIcons			: true
			, useStatusText		: false
			, closeSameLevel	: false
			, inOrder			: false
		};
		this.icon = {
			root				: treeMenuImgPath + '/base.gif'
			, folder			: treeMenuImgPath + '/folder.gif'
			, folderOpen		: treeMenuImgPath + '/folderopen.gif'
			, node				: treeMenuImgPath + '/page.gif'
			, empty				: treeMenuImgPath + '/empty.gif'
			, line				: treeMenuImgPath + '/line.gif'
			, join				: treeMenuImgPath + '/join.gif'
			, joinBottom		: treeMenuImgPath + '/joinbottom.gif'
			, plus				: treeMenuImgPath + '/plus.gif'
			, plusBottom		: treeMenuImgPath + '/plusbottom.gif'
			, minus				: treeMenuImgPath + '/minus.gif'
			, minusBottom		: treeMenuImgPath + '/minusbottom.gif'
			, nlPlus			: treeMenuImgPath + '/nolines_plus.gif'
			, nlMinus			: treeMenuImgPath + '/nolines_minus.gif'
			, question			: treeMenuImgPath + '/question.gif'
			, trash				: treeMenuImgPath + '/trash.gif'
		};
		this.obj			= objName;				
		this.aNodes			= [];					
		this.aIndent		= [];					
		this.root			= new Node(-1);			
		this.selectedNode	= null;					
		this.selectedFound	= false;				
		this.completed		= false;
	}
	/************************************
	 * Adds a new node to the node array
	 ************************************/
	dTree.prototype.add = function(id, pid, name, url, title, target, icon, iconOpen, open, menuDiv) {
		this.aNodes[this.aNodes.length] = new Node(id, pid, name, url, title, target, icon, iconOpen, open, menuDiv);
	};
	/**************************
	 * Open/close all nodes
	 **************************/
	dTree.prototype.openAll = function() {
		this.oAll(true);
	};
	dTree.prototype.closeAll = function() {
		this.oAll(false);
	};

	/********************************
	 * Outputs the tree to the page
	 ********************************/
	dTree.prototype.toString = function() {
		var str = '<div class="dtree">\n';
		if (document.getElementById) {
			if (this.config.useCookies) {
				this.selectedNode = this.getSelected();
			}
			str += this.addNode(this.root);
		} else {
			str += 'Browser not supported.';
		}
		str += '</div>';
		if (!this.selectedFound) {
			this.selectedNode = null;
		}
		this.completed = true;

		return str;
	};
	/*******************************
	 * Creates the tree structure
	 *******************************/
	
	var iii = 0;
	
	dTree.prototype.addNode = function(pNode) {
		
		var str = '';
		var n=0;
		if (this.config.inOrder) {
			n = pNode._ai;
		}
		
		for (n; n<this.aNodes.length; n++) {
			
			if (this.aNodes[n].pid == pNode.id) {
				var cn = this.aNodes[n];
				cn._p = pNode;
				cn._ai = n;
				
				//alert(n + "-1" + "-" + cn.name + "...cn._ls==========>" + cn._ls);
				
				this.setCS(cn);
				
				//alert(n + "-2" + "-" + cn.name + "...cn._ls==========>" + cn._ls);
				
				
				
				if (!cn.target && this.config.target) {
					cn.target = this.config.target;
				}
				if (cn._hc && !cn._io && this.config.useCookies) cn._io = this.isOpen(cn.id);
				if (!this.config.folderLinks && cn._hc) cn.url = null;
				if (this.config.useSelection && cn.id == this.selectedNode && !this.selectedFound) {
					cn._is = true;
					this.selectedNode = n;
					this.selectedFound = true;
				}
				
				/*
				if( cn.menuDiv == "01" ){// 폴더인 경우
					if(cn._hc == true && n > 0){					
						cn._hc = true;
					}
				}else if( cn.menuDiv == "02"){// 파일인 경우
					if(cn._hc == true && n > 0){
						cn._hc = false;
					}
				}
				*/
				
				str += this.node(cn, n);
				
				//alert(n + "-3" + "-" + cn.name + "...cn._ls==========>" + cn._ls);
				if (cn._ls) {
					break;
				}
				
				//alert("this.aNodes[n].pid===>" + this.aNodes[n].pid + "\npNode.id========>" + pNode.id + "\ncn.name=========>" + cn.name);
			}
		}
		
		//alert(str);
		return str;
	};

	/**************************************
	 * Creates the node icon, url and text
	 **************************************/
	dTree.prototype.node = function(node, nodeId) {
		var str = '<div class="dTreeNode">' + this.indent(node, nodeId);
				
		if (this.config.useIcons) {
			/*
			 1.	[A] 
			 [A] 와 [B] 가 같으면 [C] 
			 [A] 와 [B] 가 다르면  [D]
			 
			 2. [B]
			 [B-1] 와 [B-2] 가 같으면 [C]
			 [B-1] 와 [B-2] 가 다르면 [D]
			 
			  3. [D]
			[D] 가 참이면		[D-1]
			[D] 가 거짓이면 	[D-2]
			
							 [    A   ]	[  B (B-1) 		   (B-2) ]	 [      C     ]	  [    D  	   (       D-1     )  (      D-2    )]		*/
			//if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);
			/*
				node._hc가 *참 또는  menuDiv가 *'01'이면 폴더 아이콘 적용,
				node._hc가 거짓 또는 menuDiv가 *'02'이면 파일 아이콘 적용 
			*/
			if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc || (node.menuDiv == "01")) ? this.icon.folder : this.icon.node);
			if (!node.iconOpen) node.iconOpen = (node._hc || (node.menuDiv == "02")) ? this.icon.folderOpen : this.icon.node;
			if (this.root.id == node.pid) {
				node.icon = this.icon.root;
				node.iconOpen = this.icon.root;
			}
			
			str += '<img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';



		}
		
		if (node.url) {
			/******************************
			 * url이 javascript 인지 체크하여
			 * 옵션을 정해준다.
			 ******************************/
			var checkJavascriptFlag = false;
			if( node.url.toUpperCase().indexOf("JAVASCRIPT:") == 0 ) {
				checkJavascriptFlag = true;
			} else {
				checkJavascriptFlag = false;
			}
			
			//str += '<a id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" href="' + node.url + '"';
			str += '<a id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" href="#"';
			if (node.title) str += ' title="' + node.title + '"';

			if( node.target && (checkJavascriptFlag == false) ) {
				str += ' target="' + node.target + '"';
			}
			if (this.config.useStatusText) {
				str += ' onmouseover="window.status=\'' + node.name + '\';return true;" onmouseout="window.status=\'\';return true;" ';
			}
			if( this.config.useSelection && ((node._hc && this.config.folderLinks) || !node._hc)
				 && (checkJavascriptFlag == false) ) {
				str += ' onclick="javascript: setCookieNew(\'dtree_left_id\',\''+ node.id +'\',365);' + this.obj + '.s(' + nodeId + ');document.location.href=\''+ node.url +'\'"';
			}
			str += '>';
		} else if ((!this.config.folderLinks || !node.url) && node._hc && node.pid != this.root.id) {
			str += '<a href="javascript: ' + this.obj + '.o(' + nodeId + ');" class="node">';
		}
		str += node.name;
		if (node.url || ((!this.config.folderLinks || !node.url) && node._hc)) str += '</a>';
		str += '</div>';
		if (node._hc) {
			str += '<div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.pid || node._io) ? 'block' : 'none') + ';">';
			str += this.addNode(node);
			str += '</div>';
		}
		this.aIndent.pop();
		return str;
	};

	/************************************
	 * Adds the empty and line icons
	 ************************************/
	dTree.prototype.indent = function(node, nodeId) {
		var str = '';
		if (this.root.id != node.pid) {
			for (var n=0; n<this.aIndent.length; n++)
				str += '<img src="' + ( (this.aIndent[n] == 1 && this.config.useLines) ? this.icon.line : this.icon.empty ) + '" alt="" />';
			(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);
			if (node._hc) {
				str += '<a href="javascript: ' + this.obj + '.o(' + nodeId + ');"><img id="j' + this.obj + nodeId + '" src="';
				if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;
				else str += ( (node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls && this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );
				str += '" alt="" /></a>';
			} else str += '<img src="' + ( (this.config.useLines) ? ((node._ls) ? this.icon.joinBottom : this.icon.join ) : this.icon.empty) + '" alt="" />';
		}
		return str;
	};

	/********************************************************************
	 * Checks if a node has any children and if it is the last sibling
	 ********************************************************************/
	dTree.prototype.setCS = function(node) {
		var lastId;
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n].pid == node.id) node._hc = true;
			if (this.aNodes[n].pid == node.pid) lastId = this.aNodes[n].id;
		}
		
		if (lastId==node.id) node._ls = true;
	};

	/********************************************************************
	 * Returns the selected node
	 ********************************************************************/
	dTree.prototype.getSelected = function() {
		var sn = this.getCookie('cs' + this.obj);
		return (sn) ? sn : null;
	};

	// Highlights the selected node
	dTree.prototype.s = function(id) {
		
		if (!this.config.useSelection) return;
		var cn = this.aNodes[id];
		if (cn._hc && !this.config.folderLinks) return;
		if (this.selectedNode != id) {
			if (this.selectedNode || this.selectedNode==0) {
				eOld = document.getElementById("s" + this.obj + this.selectedNode);
				eOld.className = "node";
			}
			eNew = document.getElementById("s" + this.obj + id);
			eNew.className = "nodeSel";
			this.selectedNode = id;
			if (this.config.useCookies) this.setCookie('cs' + this.obj, cn.id);
		}
		//alert(cn.id);
		//setCookieNew("dtree_left_id",cn.id,365);
		//alert(document.cookie);
	};

	// Toggle Open or close
	dTree.prototype.o = function(id) {
		var cn = this.aNodes[id];
		this.nodeStatus(!cn._io, id, cn._ls);
		cn._io = !cn._io;
		if (this.config.closeSameLevel) this.closeLevel(cn);
		if (this.config.useCookies) this.updateCookie();
		
		//setCookieNew("dtree_left_id",cn.id,365);
		//alert(cn.id);
	};

	// Open or close all nodes
	dTree.prototype.oAll = function(status) {
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n]._hc && this.aNodes[n].pid != this.root.id) {
				this.nodeStatus(status, n, this.aNodes[n]._ls)
				this.aNodes[n]._io = status;
			}
		}
		if (this.config.useCookies) this.updateCookie();
	};

	// Opens the tree to a specific node
	dTree.prototype.openTo = function(nId, bSelect, bFirst) {
		if (!bFirst) {
			for (var n=0; n<this.aNodes.length; n++) {
				if (this.aNodes[n].id == nId) {
					nId=n;
					break;
				}
			}
		}
		var cn=this.aNodes[nId];
		if (cn.pid==this.root.id || !cn._p) return;
		cn._io = true;
		cn._is = bSelect;
		if (this.completed && cn._hc) this.nodeStatus(true, cn._ai, cn._ls);
		if (this.completed && bSelect) this.s(cn._ai);
		else if (bSelect) this._sn=cn._ai;
		this.openTo(cn._p._ai, false, true);
	};

	// Closes all nodes on the same level as certain node
	dTree.prototype.closeLevel = function(node) {
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n].pid == node.pid && this.aNodes[n].id != node.id && this.aNodes[n]._hc) {
				this.nodeStatus(false, n, this.aNodes[n]._ls);
				this.aNodes[n]._io = false;
				this.closeAllChildren(this.aNodes[n]);
			}
		}
	};

	// Closes all children of a node
	dTree.prototype.closeAllChildren = function(node) {
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n].pid == node.id && this.aNodes[n]._hc) {
				if (this.aNodes[n]._io) this.nodeStatus(false, n, this.aNodes[n]._ls);
				this.aNodes[n]._io = false;
				this.closeAllChildren(this.aNodes[n]);
			}
		}
	};

	// Change the status of a node(open or closed)
	dTree.prototype.nodeStatus = function(status, id, bottom) {
		eDiv	= document.getElementById('d' + this.obj + id);
		eJoin	= document.getElementById('j' + this.obj + id);
		if (this.config.useIcons) {
			eIcon	= document.getElementById('i' + this.obj + id);
			eIcon.src = (status) ? this.aNodes[id].iconOpen : this.aNodes[id].icon;
		}
		eJoin.src = (this.config.useLines)?
		((status)?((bottom)?this.icon.minusBottom:this.icon.minus):((bottom)?this.icon.plusBottom:this.icon.plus)):
		((status)?this.icon.nlMinus:this.icon.nlPlus);
		eDiv.style.display = (status) ? 'block': 'none';
	};


	// [Cookie] Clears a cookie
	dTree.prototype.clearCookie = function() {
		var now = new Date();
		var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
		this.setCookie('co'+this.obj, 'cookieValue', yesterday);
		this.setCookie('cs'+this.obj, 'cookieValue', yesterday);
	};

	// [Cookie] Sets value in a cookie
	dTree.prototype.setCookie = function(cookieName, cookieValue, expires, path, domain, secure) {
		document.cookie =
			escape(cookieName) + '=' + escape(cookieValue)
			+ (expires ? '; expires=' + expires.toGMTString() : '')
			+ (path ? '; path=' + path : '')
			+ (domain ? '; domain=' + domain : '')
			+ (secure ? '; secure' : '');
	};

	// [Cookie] Gets a value from a cookie
	dTree.prototype.getCookie = function(cookieName) {
		var cookieValue = '';
		var posName = document.cookie.indexOf(escape(cookieName) + '=');
		if (posName != -1) {
			var posValue = posName + (escape(cookieName) + '=').length;
			var endPos = document.cookie.indexOf(';', posValue);
			if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));
			else cookieValue = unescape(document.cookie.substring(posValue));
		}
		return (cookieValue);
	};

	// [Cookie] Returns ids of open nodes as a string
	dTree.prototype.updateCookie = function() {
		var str = '';
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n]._io && this.aNodes[n].pid != this.root.id) {
				if (str) str += '.';
				str += this.aNodes[n].id;
			}
		}
		this.setCookie('co' + this.obj, str);
	};

	// [Cookie] Checks if a node id is in a cookie
	dTree.prototype.isOpen = function(id) {
		var aOpen = this.getCookie('co' + this.obj).split('.');
		for (var n=0; n<aOpen.length; n++)
			if (aOpen[n] == id) return true;
		return false;
	};

	// If Push and pop is not implemented by the browser
	if (!Array.prototype.push) {
		Array.prototype.push = function array_push() {
			for(var i=0;i<arguments.length;i++)
				this[this.length]=arguments[i];
			return this.length;
		}
	};

	if (!Array.prototype.pop) {
		Array.prototype.pop = function array_pop() {
			lastElement = this[this.length-1];
			this.length = Math.max(this.length-1,0);
			return lastElement;
		}
	};

	
	function setCookieNew(c_name,value,exdays)
	{
		alert(c_name);
		alert(value);
		//var exdate=new Date();
		//exdate.setDate(exdate.getDate() + exdays);
		//var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
		document.cookie=c_name + "=" + escape(value);
		
		alert(document.cookie);
	}

	function getCookieNew(c_name)
	{
		var i,x,y,ARRcookies=document.cookie.split(";");
		for (i=0;i<ARRcookies.length;i++)
		{
		  x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
		  y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
		  x=x.replace(/^\s+|\s+$/g,"");
		  if (x==c_name)
		    {
		    return unescape(y);
		    }
		  }
	}