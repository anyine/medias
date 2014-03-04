Ext.ns("app.system");
app.system.MainView = Ext
		.extend(
				Ext.Viewport,
				{
					layout : 'border',
					chObj : {},
					initComponent : function() {
						var me = this;
						// 顶部panel
						var titlePanel = new Ext.Panel(
								{
									region : 'north',
									height : 70,
									tbar : [
											{
												xtype : 'imagebutton',
												imgPath : 'app/resource/image/biaoti.jpg'
											},
											'->',
											{
												xtype : 'imagebutton',
												imgPath : 'app/resource/image/updatesystem.png',
												listeners : {
													'click' : me.updateSystemParameter
												}
											},
											{
												xtype : 'imagebutton',
												imgPath : 'app/resource/image/programupload.png',
												listeners : {
													'click' : me.programUpload
												}
											},
											{
												xtype : 'imagebutton',
												imgPath : 'app/resource/image/modifypassword.png',
												listeners : {
													'click' : me.modifyPassword
												}
											},
											{
												xtype : 'imagebutton',
												imgPath : 'app/resource/image/exit.png',
												listeners : {
													'click' : me.clickExit
												}
											} ],
									bodyStyle : 'border-top: 0px; border-buttom: 0px;'// 去掉边线
								});

						// 主操作区panel
						var tabPanel = new Ext.TabPanel(
								{
									region : 'center',
									resizeTabs : true, // turn on tab resizing
									minTabWidth : 115,
									tabWidth : 135,
									enableTabScroll : true,
									margins : '0 0 0 0',
									activeTab : 0,// 默认显示第一页
									items : [ {
										xtype : 'panel',
										title : '首 页',
										html : '<image src="app/resource/image/right.jpg" style="width:100%; height:100%"/>'
									} ]
								});

						// 左侧菜单Panel
						var menuPanel = new Ext.Panel({
							region : 'west',
							xtype : 'panel',
							margins : '0 0 0 0',
							title : '功   能   菜   单',
							iconCls : 'gongneng',
							width : 170,
							split : false,
							collapsible : true,
							layout : 'accordion',
							layoutConfig : {
								activeOnTop : false,
								animate : true,
								titleCollapse : true
							},
							items : [ {
								xtype : 'panel',
								title : '任务信息',
								layout : 'fit',
								iconCls : 'phytrain',
								items : [ {
									xtype : 'menutree1',
									listeners : {
										'click' : me.click
									}
								} ]
							}, {
								xtype : 'panel',
								title : '基础信息',
								layout : 'fit',
								iconCls : 'phytrain',
								items : [ {
									xtype : 'menutree2',
									listeners : {
										'click' : me.click
									}
								} ]
							} ]
						});

						// 每秒钟获取一次时间
						// window
						// .setInterval(
						// function() {
						// Ext.Ajax
						// .request({
						// url :
						// 'cn/wizool/bank/servlet/InterfaceServlet?method=getDate',
						// success : function(
						// response) {
						// var date = Ext
						// .decode(response.responseText).date;
						// // document
						// // .getElementById(
						// // 'date')
						// // .setText(
						// // date);
						// },
						// failure : function() {
						// }
						// });
						// }, 1000);new Ext.Toolbar.TextItem('服务器时间:'), {
						// xtype : 'label',
						// id : 'date',
						// text : ''
						// },

						// 底部工具栏panel
						var toolbarPanel = new Ext.Panel({
							region : 'south',
							xtype : 'panel',
							split : false,
							height : 30,
							collapsible : true,
							margins : '0 0 0 0',
							bbar : [ {
								xtype : "tbfill"
							}, new Ext.Toolbar.TextItem('微卓软件'), {
								xtype : 'tbseparator'
							}, {
								pressed : false,
								text : '联系我们',
								iconCls : 'tabs'
							} ]
						});
						me.items = [ titlePanel, menuPanel, tabPanel,
								toolbarPanel ];
						app.system.MainView.superclass.initComponent.apply(
								this, arguments);
					},
					click : function(node, e) {
						var me = this;
						var win = me.findParentByType();
						var attr = node.attributes;
						var tabPanel = win.findByType('tabpanel')[0];
						if (attr.type == "window") {
							eval("var win =  new " + attr.file
									+ "();win.show()");
						} else {
							var fileUrl = attr.file;
							var tabItem = eval("new "
									+ fileUrl
									+ "({closable : true,iconCls : attr.iconCls2,type:attr.type,id:attr.type,title:attr.text});");
							if (tabPanel.getComponent(win.chObj[tabItem.title]) == null) {
								tabPanel.add(tabItem);
								tabPanel.setActiveTab(tabItem);
								win.chObj[tabItem.title] = tabItem.id;
							} else {
								tabPanel.setActiveTab(win.chObj[tabItem.title]);
							}
						}
					},
					updateSystemParameter : function() {
						var win = new app.system.UpdateSystemParameter({
							title : '系统更新',
							iconCls : 'updatesystem'
						});
						win.show();
					},
					programUpload : function() {
						var win = new app.system.ProgramUpload({
							title : '程序上传',
							iconCls : 'programupload'
						});
						win.show();
					},
					clickExit : function() {
						Ext.MessageBox
								.confirm(
										'提示',
										'您确定退出系统吗？',
										function(btn) {
											if (btn == 'yes') {
												document.cookie = "userName=null;path=/";
												window.location.href = "/bank";
											}
										});
					},
					modifyPassword : function() {
						var win = new app.system.ModifyPassword({
							title : '修改密码',
							iconCls : 'modifypassword'
						});
						win.show();
					}
				});
