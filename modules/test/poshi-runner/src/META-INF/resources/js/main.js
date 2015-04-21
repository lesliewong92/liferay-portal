var loggerInterface = YUI();
loggerInterface.ready(
		'anim',
		'aui-button',
		'aui-node',
		'event',
		'resize',
		'transition',
		function(A) {
			var currentScope;
			var commandLogScope;
			var fails;
			var commandLogId;

			var sidebar = A.one('.sidebar');
			var xmlLog = A.one('.xml-log');

			var WIN = A.getWin();

			function init() {
				sidebar.delegate(
					'click',
					linkFunction,
					'.linkable .line-container'
				);

				sidebar.delegate(
					'click',
					collapseToggle,
					'.expand-toggle',
					null,
					null,
					true
				);

				xmlLog.delegate(
					'click',
					collapseToggle,
					'.btn-collapse'
				);

				xmlLog.delegate(
					'click',
					collapseToggle,
					'.btn-var'
				);

				xmlLog.delegate(
					'mouseover',
					scopeHover,
					testScopeable,
					null,
					true
				);

				xmlLog.delegate(
					'click',
					scopeSelect,
					testScopeable
				);

				xmlLog.delegate(
					'click',
					showError,
					'.error-btn'
				);

				xmlLog.delegate(
					'click',
					showError,
					'.screenshot-btn'
				);

				xmlLog.delegate(
					'click',
					fullScreen,
					'.screenshot-container img, .fullscreen-image'
				)

				var logBtn = sidebar.all('.btn-command-log');

				logBtn.on(
					'click',
					commandLogToggle
				);

				var sidebarBtn = sidebar.one('.btn-sidebar');

				sidebarBtn.on(
					'click',
					resizeXmlLog
				);

				var jumpToBtn = sidebar.one('.btn-jump-to-error');

				jumpToBtn.on(
					'click',
					expandTree
				);

				if (!sidebar.hasClass('finished')) {
					commandLogToggle();
				}
				// else {
				// 	allNodes = xmlLog.all('[style]');
				// 	allNodes.each(
				// 		function(node) {
				// 			node.attr('style', '');
				// 		}
				// 	);
				// }
			}

			init();

			function expandTree(event, node, noScroll) {
				if (!node) {
					node = fails.last();
				}
				var tree = node.ancestors('.child-container');

				var temp = expandLoop(tree, node, noScroll);
			}

			function expandLoop(tree, target, noScroll) {
				var timing = 0;

				var node = tree.splice(0, 1);

				node = node.item(0);

				if (node.hasClass('collapse')) {
					collapseToggle(null, node);

					timing = 200;
				}
				if(tree.size() > 0) {
					setTimeout(expandLoop, timing, tree, target, noScroll);
				}
				else if(!noScroll) {
					scrollToNode(target);
				}
			}

			function transitionCommandLog(commandLog) {
				var newWidth = '20%';

				if (commandLogId) {
					newWidth = '40%';
				}

				sidebar.setStyle('width', newWidth);
				resizeXmlLog(null, true);
				commandLog.toggleClass('collapse');

				var lastLog = commandLog.one('ul:last-child');

				// if (lastLog) {
				// 	var lastLine = lastLog.previous();

				// 	var functionId = lastLine.attr('data-functionLinkId');
				// }

				if (lastLog && lastLog.hasClass('collapse')) {
					collapseToggle(null, lastLog, true);
				}

				if (commandLogScope) {
					scrollToNode(commandLogScope.item(0), true)
				}
			}

			function commandLogToggle(event, commandLog) {
				var logId;

				if (event) {
					var btn = event.currentTarget;

					commandLog = getLink(btn, '.command-log', 'data-logId', sidebar);
				}
				else {
					if (!commandLog) {
						commandLog = A.one('.command-log');
					}

					var btn = getLink(commandLog, '.btn-command-log', 'data-logId', sidebar);
				}
				btn.toggleClass('toggle');

				var logId = commandLog.attr('data-logId');

				if (!commandLogId) {
					commandLogId = logId;
				}
				else {
					if (commandLogId === logId) {
						commandLogId = null;
					}
					else {
						var currentActiveLog = sidebar.one('.command-log[data-logId=' + commandLogId + ']')
						commandLogToggle(null, currentActiveLog);
						commandLogId = logId;
					}
				}

				refreshXmlClasses();

				transitionCommandLog(commandLog);

				fails = xmlLog.all('.fail');

				if (fails) {
					fails.each(xmlLogRefresh);
				}

				var body = A.one('body');

				body.toggleClass('link-run-log');
			}

			function refreshXmlClasses(id) {
				var status = ['pass', 'pending', 'fail']
				var selector = 'data-status' + commandLogId;

				for (var i = 0; i < status.length; i++) {
					var nodes
					var status

					if (!id) {
						nodes = A.all('[' + selector + '="' + status[i] + '"]');
						status = status[i];
					}
					else {
						nodes = xmlLog.one('#' + id);
					}
					if (nodes) {
						status = nodes.attr(selector);
						nodes.removeClass('pending');
						nodes.removeClass('fail');
						nodes.removeClass('pass');
						nodes.addClass(status);
					}
				}
			}

			function xmlLogRefresh(node) {
				expandTree(null, node);
				scopeSelect(null, node);
			}

			function resizeXmlLog(event, open) {
				if (event) {
					var currentTarget = event.currentTarget;
				}

				var xmlLogWidth = 100;
				var translation = 100;

				if (open || (currentTarget && currentTarget.hasClass('toggle'))) {
					var sidebarWidth = sidebar.getStyle('width');

					if (sidebarWidth.indexOf('%') === -1) {
						sidebarWidth = 100 * (parseFloat(sidebarWidth) / WIN.width());
					}
					else {
						sidebarWidth = parseFloat(sidebarWidth);
					}
					xmlLogWidth = (100 - sidebarWidth);

					translation = 0;
				}
				sidebar.setStyle('transform', 'translateX(' + translation + '%)');
				xmlLog.setStyle('width', xmlLogWidth + '%');

				if (currentTarget) {
					currentTarget.toggleClass('toggle');
				}
			}

			function manageHeightDiff(heightDiff, node) {
				var nodeList = node.ancestors('[data-prevHeight]');

				if (nodeList.size() > 0) {
					for (var i = 0; i < nodeList.size(); i++) {
						var ancestorNode = nodeList.item(i);

						var prevHeight = ancestorNode.attr('data-prevHeight');

						ancestorNode.attr('data-prevHeight', (parseInt(prevHeight, 10) + heightDiff));
					}
				}
			}

			function linkFunction(event, node) {
				if (event) {
					var currentTarget = event.currentTarget.ancestor();
				}
				else {
					var currentTarget = node;
				}

				var linkedFunction = getLink(currentTarget, 'li', 'data-functionLinkId', xmlLog);

				if (currentScope) {
					currentScope.removeClass('current-scope');
				}

				parseCommandLog(currentTarget, true)

				linkedFunction.addClass('current-scope');

				currentScope = linkedFunction;

				expandTree(null, linkedFunction);
			}

			function collapseToggle(event, collapseContainer, inSidebar) {
				var collapseBtn;
				var scope = xmlLog;
				var resetHeights = true;

				if (inSidebar) {
					resetHeights = false;
					scope = sidebar;
				}

				if (!collapseContainer) {
					collapseBtn = event.currentTarget;

					collapseContainer = getLink(collapseBtn, '.child-container', 'data-btnLinkId', scope);
				}
				else {
					collapseBtn = getLink(collapseContainer, '.btn', 'data-btnLinkId', scope);
				}

				var collapsed = collapseTransition(collapseContainer, resetHeights);

				if (collapsed) {
					collapseBtn.toggleClass('toggle');
				}
			}

			var running;

			function collapseTransition(targetNode, resetHeights) {
				var height;

				if (targetNode && (!running || !running.contains(targetNode))) {
					var collapsing = targetNode.getStyle('height') != '0px';
					if (collapsing) {
						height = targetNode.outerHeight();

						targetNode.setStyle('height', height);

						running = targetNode;
						targetNode.addClass('transitioning');
					}
					else {
						var lastChild = targetNode.getDOMNode().lastElementChild;

						lastChild = A.Node(lastChild);

						targetNode.removeClass('collapse');
						targetNode.addClass('transitioning');

						var lastChildY = lastChild.getY();
						var lastChildHeight = lastChild.innerHeight();
						var lastChildBottomY = lastChildY + lastChildHeight + 1;

						height = (lastChildBottomY - targetNode.getY());
					}

					getTransition(targetNode, height, collapsing, resetHeights);

					return true;
				}
			}

			function fullScreen(event) {
				var node = event.currentTarget;

				var src = node.attr('src');
				var fullscreenDiv = A.one('.fullscreen-image');
				if (fullscreenDiv.hasClass('toggle')) {
					fullscreenDiv.append(A.Node.create('<img alt="fullscreen screenshot" src="' + src + '">'));
				}
				else {
					fullscreenDiv.one('*').remove(true);
				}
				fullscreenDiv.toggleClass('toggle');
			}

			function getTransition(targetNode, height, collapsing, resetHeights) {
				var transDuration = (Math.pow(height, 0.35) / 15);

				var ease = 'ease-in';
				var newHeight = height;

				if (collapsing) {
					newHeight = 0;

					ease = 'ease-out';
				}

				targetNode.transition(
					{
						height: {
							duration: transDuration,
							easing: ease,
							value: newHeight
						}
					},
					function() {
						targetNode.removeClass('transitioning');
						callback(this, collapsing);
						running = null;
					}
				);
			}

			function callback(node, collapsing, inSidebar) {
				var height = 'auto';

				if (inSidebar) {
					height = '100%';
				}
				if (collapsing) {
					node.addClass('collapse');
				}
				else {
					node.setStyle('height', height);
				}
			}

			function getLink(node, selector, attrName, scope, returnAll) {
				var linkId = node.attr(attrName);

				if (!scope) {
					scope = A;
				}

				var links;
				var attrSelector = (selector + '[' + attrName + '=' + linkId + ']');

				if (!returnAll) {
					links = scope.one(attrSelector);
				}
				else {
					links = scope.all(attrSelector);
				}
				return links;
			}

			var prevHover;

			function scopeHover(event, enter) {
				var currentTarget = event.currentTarget;

				if (prevHover) {
					prevHover.removeClass('scoped');
				}
				currentTarget.addClass('scoped');
				prevHover = currentTarget;

					event.stopPropagation();
			}

			function scopeSelect(event, node) {
				var scope
				var clickable = true;

				if (!event) {
					scope = node;
				}
				else {
					var scope = event.currentTarget;
					clickable = testClickable(event.target);
					event.stopPropagation();
				}

				if (clickable) {

					if (currentScope) {
						currentScope.removeClass('current-scope');
					}

					currentScope = scope;

					scope.addClass('current-scope');

					parseCommandLog(scope);

					scopeSidebar();
				}
			}

			function parseCommandLog(scope, noLookUp) {
				if (commandLogScope) {
					commandLogScope.removeClass('current-scope');
				}
				commandLogScope = new A.NodeList();

				if (scope.hasClass('macro')) {
					var macroScope = scope.all('[data-functionLinkId]');

					macroScope.each(
						scopeCommandLog
					);
				}
				else {
					scopeCommandLog(scope, null, null, noLookUp);
				}
				var position = null;
				if (!noLookUp) {
					position = scope;
				}

				scrollToNode(commandLogScope.item(0), true, position);
			}

			function scopeCommandLog(scope, index, nodeList, noLookUp) {
				if (!noLookUp) {
					scope = getLink(scope, '.linkable', 'data-functionLinkId', sidebar, true);

					while(scope.size() > 0) {
						var node = scope.pop()

						commandLogScope.push(node);
					}
				}
				else {
					commandLogScope.push(scope);
				}

				commandLogScope.addClass('current-scope');
			}

			function scopeSidebar() {
				if (currentScope) {
					var sidebarScopeName = sidebar.one('.scope-type .scope-name');
					var sidebarScopeTitle = sidebar.one('.scope-type .title');
					var sidebarParameterTitle = sidebar.one('.parameter .title');
					var sidebarParameterList = sidebar.one('.parameter .parameter-list');

					var scopeNames = currentScope.all('> .line-container .name');
					var scopeTypes = currentScope.all('> .line-container .tag-type');

					var scopeType = scopeTypes.item(0);
					var scopeName = scopeNames.item(0);

					if (scopeName) {
						scopeName = scopeName.html();
					}
					else {
						var scopeName = currentScope.one('.testCaseCommand');

						if (scopeName) {
							scopeName = scopeName.html();
						}
					}
					if (scopeType && scopeType.html() != 'name') {
						scopeType = scopeType.html();
					}
					else {
						scopeType = currentScope.one('> .line-container .action-type');

						if (scopeType) {
							scopeType = scopeType.html();
						}
						else {
							scopeType = 'test-case'
						}

					}

					sidebarScopeName.html(scopeName);
					sidebarScopeTitle.html(scopeType);

					sidebarParameterList.all('> *').remove(true);

					var parameterCount;

					sidebarParameterTitle.removeClass('hidden');

					if (scopeType === 'macro') {
						var parameters = currentScope.all('> .line-container .parameter-container .parameter-value');

						parameterCount = parameters.size();

						for (var i = 0; i < parameterCount; i += 2) {
							sidebarParameterList.append(A.Node.create('<li class="parameter-name">' + parameters.item(i).html() + '</div>'));
							sidebarParameterList.append(A.Node.create('<li class="parameter-value">' + parameters.item(i + 1).html() + '</div>'));
						}
					}
					else if (scopeType === 'function') {
						parameterCount = (scopeNames.size() - 1);

						for (var i = 1; i <= parameterCount; i++) {
							sidebarParameterList.append(A.Node.create('<li class="parameter-name">' + scopeTypes.item(i).html() + '</div>'));
							sidebarParameterList.append(A.Node.create('<li class="parameter-value">' + scopeNames.item(i).html() + '</div>'));
						}
					}
					else {
						sidebarParameterTitle.addClass('hidden');
					}
				}
			}

			function scrollToNode(node, inSidebar, matchNode) {
				var scrollNode = WIN;

				if (inSidebar) {
					scrollNode = sidebar.one('.command-log[data-logId=' + commandLogId + ']');
				}

				if (node && scrollNode) {
					var nodeY = node.getY();

					if (inSidebar) {
						nodeY = (nodeY - scrollNode.one('.divider-line').getY());
					}
					var offset;
					var halfNodeHeight = (node.innerHeight() / 2);

					if (!matchNode) {
						var winHalf = (WIN.height() / 2);
						offset = (winHalf - halfNodeHeight);
					}
					else {
						var position = matchNode.getY();
						offset = (position - window.scrollY);
					}
					var yDistance = (nodeY - offset);

					var scroll = new A.Anim(
						{
							duration: 2,
							easing: 'easeOutStrong',
							node: scrollNode,
							to: {
								scroll: [0, yDistance]
							}
						}
					);

					scroll.run();
				}
			}

			function showError(event) {
				var currentTarget = event.currentTarget;

				currentTarget.toggleClass('toggle');

				var errorPanel = getLink(currentTarget, '.errorPanel', 'data-errorLinkId', xmlLog);

				if (errorPanel) {
					errorPanel.toggleClass('toggle');
				}
			}

			function testClickable(testNode) {
				return !testNode.test('.btn, .btn-container');
			}

			function testScopeable(testNode) {
				return testNode.hasClass('macro') || testNode.hasClass('function') || testNode.hasClass('test-group');
			}

			function refreshXmlError(command) {
				var errorContainer = command.one('.error-container');
				if (errorContainer) {
					var consoleLog = errorContainer.one('.console');
					var screenshot = errorContainer.one('.screenshots');
					var xmlLink = getLink(command, '.line-group', 'data-functionLinkId', xmlLog);
					var btnContainer = xmlLink.one('.btn-container');

					var imgBefore = screenshot.one('.before');
					var imgAfter = screenshot.one('.after');

					var screenshotError = screenshot.attr('data-errorLinkId')
					var consoleError = consoleLog.attr('data-errorlinkid');

					console.log(consoleLog);

					btnContainer.append(A.Node.create('<button class="btn screenshot-btn" data-errorlinkid="' + screenshotError + '"><div class="btn-content"></div></button>'));
					btnContainer.append(A.Node.create('<button class="btn error-btn" data-errorlinkid="' + consoleError + '"><div class="btn-content"></div></button>'));

					xmlLink.prepend(screenshot);
					xmlLink.append(consoleLog);
				}
			}

			function updateLog(id, isFail) {
				if (isFail) {
					var test = xmlLog.one('#' + id);
					var test2 = test.attr('data-status01');
					console.log(test2);
				}
				var commandLog = sidebar.one('.command-log[data-logId="' + commandLogId +'"]');
				var latestCommand = commandLog.one('.line-group:last-child');

				if (latestCommand) {
					linkFunction(null, latestCommand);
				}
				if (latestCommand.hasClass('failed')) {
					var latestFailure = latestCommand;

					refreshXmlError(latestFailure);
				}
				refreshXmlClasses(id);
			}

			function updateXml(id) {
				refreshXmlClasses(id);
				var linkedLine = xmlLog.one('#' + id);
				var container = linkedLine.one('> .child-container');

				var firstLine = linkedLine.one('.line-container');

				if (container && container.hasClass('collapse')) {
					collapseToggle(null, container);
				}
				scrollToNode(firstLine);
			}

			function updateXmlClosing(id) {
				var linkedLine = xmlLog.one('#' + id);
				var closingLine = linkedLine.one('> .line-container:last-child');
				refreshXmlClasses(id);
				var container = linkedLine.one('> .child-container');

				if (container && !container.hasClass('collapse')) {
					collapseToggle(null, container);
				}
			}

			A.on(
				'line-started',
				updateXml
			);

			A.on(
				'line-complete',
				updateXmlClosing
			);

			A.on(
				'command-complete',
				updateLog
			);
		}
	);

