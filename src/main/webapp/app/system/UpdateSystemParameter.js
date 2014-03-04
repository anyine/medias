Ext.ns('app.system');
app.system.UpdateSystemParameter = Ext
		.extend(
				Ext.Window,
				{
					title : '系统参数',
					buttonAlign : 'center',
					width : 300,
					height : 150,
					initComponent : function() {
						var me = this;
						Ext.Ajax
								.request({
									url : 'cn/wizool/bank/servlet/InterfaceServlet?method=getSystemParameter',
									success : function(response) {
										var text = Ext
												.decode(response.responseText);
										me.findByType('textfield')[0]
												.setValue(text.version);
										me.findByType('textfield')[1]
												.setValue(text.reboot);
									}
								});

						me.items = [ {
							xtype : 'form',
							bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
							defaults : {
								labelWidth : 40
							},
							defaultType : 'textfield',
							items : [ {
								fieldLabel : '版本号',
								name : 'version'
							}, {
								fieldLabel : '重启号',
								name : 'reboot'
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
						app.system.UpdateSystemParameter.superclass.initComponent
								.apply(this, arguments);
					},
					save : function(button) {
						var me = this;
						var win = button.findParentByType();
						var form = win.findByType('form')[0];
						form
								.getForm()
								.submit(
										{
											url : 'cn/wizool/bank/servlet/InterfaceServlet?method=updateSystemParameter',
											method : 'POST',
											timeout : 300,
											success : function(form, action) {
												var flag = action.result;
												if (flag) {
													Ext.example.msg("提示",
															"操作成功");
													win.close();
												}
											},
											failure : function(form, action) {
												var result = action.result;
												Ext.Msg.alert("提示",
														result.message);
											}
										});
					}
				});
