Ext
		.onReady(function() {
			var titlebar = new Ext.Toolbar({
				height : 100,
				listeners : {
					"render" : function() {
						var biaoti = new Ext.ux.ImageButton({
							imgPath : 'frame/Images/ImageButton/biaoti.png',
							imgWidth : 480,
							imgHeight : 50
						});
						titlebar.add(biaoti);
						var exit = new Ext.ux.ImageButton({
							imgPath : 'frame/Images/ImageButton/zhuxiao.png',
							imgWidth : 50,
							imgHeight : 50,
							imgText : '退出系统',
							tplHTML : '<style>'
						});
						titlebar.add(exit);
					}
				}
			});

			// 顶部panel
			var titlePanel = new Ext.Panel({
				region : 'north',
				xtype : 'panel',
				tbar : titlebar, // raul?
				bodyStyle : 'border-top: 0px; border-buttom: 0px;'// 去掉边线
			});

			// 主操作区panel
			var mainPanel = new Ext.TabPanel(
					{
						region : 'center',
						resizeTabs : true, // turn on tab resizing
						minTabWidth : 115,
						tabWidth : 135,
						enableTabScroll : true,
						margins : '3 3 0 3',
						width : 600,
						height : 250,
						activeTab : 0,// 默认显示第一页
						items : [ {
							xtype : 'panel',
							title : '首页',
							width : 1000,
							style : 'background: transparent;',// 背景颜色为透明
							bodyStyle : 'background:url(frame/Images/background/right.jpg) no-repeat left top; width: 1000;'

						} ]
					});

			// 左侧菜单Panel
			var menuPanel = new Ext.Panel({/* the west region */
				region : 'west',
				xtype : 'panel',
				margins : '3 0 0 3',
				title : '功   能   菜   单',
				iconCls : 'gongneng',
				width : 200,
				split : false,
				collapsible : true,
				layout : 'accordion',
				layoutConfig : {
					activeOnTop : false,
					animate : true,
					titleCollapse : true
				}
			});

			// 底部工具栏panel
			var toolbarPanel = new Ext.Panel({
				region : 'south',
				xtype : 'panel',
				split : false,
				height : 30,
				collapsible : true,
				margins : '3 0 3 3',
				bbar : [ new Ext.Toolbar.TextItem('工具栏 '), {
					xtype : "tbfill"
				}, new Ext.Toolbar.TextItem('汇金软件'), {
					xtype : 'tbseparator'
				}, {
					pressed : false,
					text : '联系我们',
					iconCls : 'tabs'
				} ]
			});

			var viewport = new Ext.Viewport({
				layout : 'border',// 设置布局为border布局
				items : [ titlePanel, menuPanel, mainPanel, toolbarPanel ]
			});
		});