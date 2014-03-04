Ext.ns('app.system');
app.system.ModifyPassword = Ext.extend(Ext.Window, {
			title : '修改密码',
			buttonAlign : 'center',
			width : 300,
			height : 180,
			initComponent : function() {
				var me = this;
				me.items = {
					xtype : 'form',
					bodyStyle : 'padding: 20px; background-color: #DFE8F6',
					defaults : {
						labelWidth : 40,
						allowBlank : false
					},
					defaultType : 'textfield',
					items : [{
								fieldLabel : '原密码',
								inputType : 'password',
								name : 'oldPassword',
								blankText : "用户不允许为空"
							}, {
								fieldLabel : '新密码',
								inputType : 'password',
								name : 'newPassword'
							}, {
								fieldLabel : '确认密码',
								inputType : 'password',
								name : 'newPasswordok',
								listeners : {
									specialkey : function(field, e) {
										if (e.getKey() == Ext.EventObject.ENTER) {
											me.login();
										}
									}
								},
								validator : function() {
									var value1 = me.findByType('textfield')[1]
											.getValue();
									var value2 = me.findByType('textfield')[2]
											.getValue();
									if (value1 == value2) {
										return true;
									} else {
										return false;
									}
									return false;
								}
							}]
				}

				me.buttons = [{
							text : '提交',
							handler : me.login,
							scope : me
						}, {
							text : '重置',
							handler : function() {
								me.findByType("form")[0].getForm().reset();
							}
						}]

				app.system.ModifyPassword.superclass.initComponent.apply(this,
						arguments);
			},
			login : function() {
				var me = this;
				var form = me.findByType('form')[0];
				form.getForm().submit({
							url : 'cn/wizool/bank/servlet/LoginServlet?method=modifyPassword',
							success : function(form, action) {
								var result = action.result;
								if (result.success) {
									me.close();
									Ext.example.msg("提示", "操作成功");
									var view = new app.system.MainView();
								} 
							},
							failure : function(form, action) {
								var result = action.result;
								Ext.Msg.alert("提示", result.message);
							}
						});
			}
		});