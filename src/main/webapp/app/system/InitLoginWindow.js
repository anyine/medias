Ext.ns('app.system');
app.system.InitLoginWindow = Ext.extend(Ext.Window, {
			title : '管理员注册',
			buttonAlign : 'center',
			width : 320,
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
								fieldLabel : '用户名称',
								name : 'name',
								blankText : "用户不允许为空"
							}, {
								fieldLabel : '登录密码',
								inputType : 'password',
								name : 'password'
							}, {
								fieldLabel : '登录密码',
								inputType : 'password',
								name : 'passwordok',
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
							text : '注册',
							handler : me.login,
							scope : me
						}, {
							text : '重置',
							handler : function() {
								me.findByType("form")[0].getForm().reset();
							}
						}]

				app.system.InitLoginWindow.superclass.initComponent.apply(this,
						arguments);
			},
			login : function() {
				var me = this;
				var form = me.findByType('form')[0];
				form.getForm().submit({
							url : 'cn/wizool/bank/servlet/InitServlet?method=register',
							success : function(form, action) {
								var result = action.result;
								if (result.success) {
									me.close();
									Ext.example.msg("提示", "注册成功");
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