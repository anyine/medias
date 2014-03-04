Ext.ns('app.view.system');
app.view.system.KillPowerWindow = Ext
		.extend(
				Ext.Window,
				{
					title : '编辑定时关机',
					width : 490,
					buttonAlign : 'center',
					iconCls : 'create',
					modal : true,
					initComponent : function() {
						var me = this;

						Ext.Ajax
								.request({
									url : 'cn/wizool/bank/servlet/MediaServlet?method=getKillPower',
									success : function(response) {
										var text = Ext
												.decode(response.responseText);

										me.findByType('textfield')[1]
												.setValue(text.dispatch);
										me.findByType('hidden')[0]
												.setValue(text.enabled);
										if (text.enabled == 'true') {
											me.findByType('combo')[0]
													.setValue('开');
										} else {
											me.findByType('combo')[0]
													.setValue('关');
										}
									}
								});

						var typeSt = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '开', '开' ], [ '关', '关' ] ]
						});
						me.items = [ {
							xtype : 'form',
							bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
							defaults : {
								labelWidth : 40,
								anchor : '97%'
							},
							items : [
									{
										xtype : 'textfield',
										fieldLabel : '选择机构',
										value : '点击查看',
										readOnly : true,
										listeners : {
											focus : function() {
												var win = new app.view.dept.DeptCheckWindow(
														{
															idsSelected : me.deptIds,
															classify : 'taskSelectMachine',
														});
												win
														.on(
																'beforehide',
																function() {
																	me
																			.findByType('hidden')[1]
																			.setValue(this.ids);
																});
												win.show();
											}
										}
									},
									{
										xtype : 'textfield',
										allowBlank : false,
										fieldLabel : '调度方式',
										name : 'dispatch',
										listeners : {
											focus : function() {
												var d = me
														.findByType('textfield')[1];
												var dispatch = d.getValue();
												var win = new app.view.media.DispatchWindow(
														{
															title : '编辑调度方式',
															dispatch : dispatch
														});
												win
														.on(
																'beforeclose',
																function() {
																	d
																			.setValue(this.values);
																});
												win.show();
											}
										}
									},
									{
										xtype : 'combo',
										fieldLabel : '调度开关',
										store : typeSt,
										editable : false,
										allowBlank : false,
										triggerAction : 'all',
										valueField : 'name',
										mode : 'local',
										displayField : 'name',
										name : 'type',
										width : 80,
										listeners : {
											select : function(a, b) {
												var s = b.get('name');
												if (s == "开") {
													me.findByType('hidden')[0]
															.setValue('true');
												} else if (s == "关") {
													me.findByType('hidden')[0]
															.setValue('false');
												}
											}
										}
									}, {
										xtype : 'hidden',
										name : 'enabled'
									}, {
										xtype : 'hidden',
										name : 'dept'
									} ]
						} ];

						me.buttons = [ {
							xtype : 'button',
							text : '保存',
							handler : me.save,
							scope : me
						}, {
							xtype : 'button',
							text : '取消',
							handler : function(button) {
								me.close();
							}
						} ];
						app.view.system.KillPowerWindow.superclass.initComponent
								.apply(this, arguments);
					},
					save : function() {
						var me = this;
						var form = me.findByType('form')[0];
						form
								.getForm()
								.submit(
										{
											url : 'cn/wizool/bank/servlet/MediaServlet?method=killPower',
											method : 'POST',
											success : function(form, action) {
												var flag = action.result;
												if (flag) {
													Ext.example.msg("提示",
															"操作成功");
													me.close();
												} else {
													Ext.Msg.alert("提 示",
															"程序异常，请联系管理员！");
												}
											}
										});
					}
				});