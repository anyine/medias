Ext.ns('app.system');
app.system.UploadPicture = Ext
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
								name : 'file',
								inputType : 'file'
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
						app.system.UploadPicture.superclass.initComponent
								.apply(this, arguments);
					},
					save : function(button) {
						var me = this;
						var win = button.findParentByType();
						var form = win.findByType('form')[0];
						var wait = new app.system.WaitWindow();
						form.getForm().submit({
							url : 'cn/wizool/bank/servlet/InterfaceServlet',
							method : 'POST',
							params : {
								method : 'upload',
								id : 'e5aa3164-58f6-4193-993f-ceca3b13803c'
							},
							timeout : 300,
							success : function(form, action) {
								wait.show();
								var flag = action.result;
								if (flag) {
									wait.close();
									Ext.example.msg("提示", "操作成功");
									win.close();
								}
							},
							failure : function(form, action) {
								var result = action.result;
								Ext.Msg.alert("提示", result.message);
							}
						});
					}
				});
