Ext.ns('app.system');
app.system.Login = Ext.extend(Ext.Window, {
	title : '登录窗口',
	buttonAlign : 'center',
	width : 360,
	height : 158,
	initComponent : function() {
		var me = this;
		me.items = {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			defaults : {
				labelWidth : 40,
				width : 180,
				allowBlank : false
			},
			defaultType : 'textfield',
			items : [ {
				fieldLabel : '用户名',
				name : 'userName',
				allowBlank : false,
				blankText : '用户名不允许为空'
			}, {
				fieldLabel : '登录密码',
				inputType : 'password',
				name : 'password',
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							me.login();
						}
					}
				}
			} ]
		}

		me.buttons = [ {
			text : '登录',
			handler : me.login,
			scope : me
		}, {
			text : '重置',
			handler : function() {
				me.findByType('form')[0].getForm().reset();
			}
		} ]

		app.system.Login.superclass.initComponent.apply(this, arguments);
	},
	login : function() {
		var form = this.findByType('form')[0];
		var me = this;
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/LoginServlet?method=login',
			success : function(form, action) {
				var result = action.result;
				if (result.success) {
					me.close();
					var viewport = new app.system.MainView();
				} else {
					Ext.example.msg("提示", result.message);
				}
				me.close();
			},
			failure : function(form, action) {
				var result = action.result;
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});
