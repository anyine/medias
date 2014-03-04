Ext.ns('app.system');
app.system.ProgramUpload = Ext
		.extend(
				Ext.Window,
				{
					title : '系统更新',
					buttonAlign : 'center',
					width : 300,
					height : 140,
					initComponent : function() {
						var me = this;
						me.items = [ {
							xtype : 'form',
							fileUpload : true,
							bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
							defaults : {
								labelWidth : 40,
								allowBlank : false,
								anchor : '96%'
							},
							defaultType : 'textfield',
							items : [ {
								fieldLabel : '文件',
								name : 'url',
								inputType : 'file',
								validator : function(a) {
									if (a.toUpperCase().indexOf(
											".xls".toUpperCase()) > -1
											|| a.toUpperCase().indexOf(
													".zip".toUpperCase()) > -1)
										return true
									else
										return false;
								}
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
						app.system.ProgramUpload.superclass.initComponent
								.apply(this, arguments);
					},
					save : function(button) {
						var me = this;
						var win = button.findParentByType();
						var form = win.findByType('form')[0];
						var wait = new app.system.WaitWindow();
						form
								.getForm()
								.submit(
										{
											url : 'cn/wizool/bank/servlet/DocumentServlet?method=programUpload',
											method : 'POST',
											timeout : 300,
											success : function(form, action) {
												wait.show();
												var flag = action.result;
												if (flag) {
													wait.close();
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
