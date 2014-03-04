Ext.ns('app.view.dept');
app.view.dept.UserEditWindow = Ext.extend(Ext.Window, {
	title : '编辑',
	buttonAlign : 'center',
	modal : true,
	initComponent : function() {
		var me = this;
		me.items = [ {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			defaults : {
				labelWidth : 40
			},
			defaultType : 'textfield',
			items : [ {
				xtype : 'textfield',
				fieldLabel : '选择机构',
				value : '点击查看',
				readOnly : true,
				listeners : {
					focus : function() {
						var win = new app.view.dept.BranchCheckWindow();
						win.on('beforehide', function() {
							me.findByType('hidden')[0].setValue(this.id);
							me.findByType('textfield')[0].setValue(this.name);
						});
						win.show();
					}
				}
			}, {
				fieldLabel : '名称',
				name : 'name'
			}, {
				xtype : 'hidden',
				name : 'pid'
			} ]
		} ];
		me.buttons = [ {
			text : '提交',
			handler : me.save,
			scope : me
		}, {
			text : '取消',
			handler : function() {
				me.close();
			}
		} ]
		app.view.dept.UserEditWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/DepartmentServlet?method=createUser',
			success : function(form, action) {
				var result = action.result;
				me.store.load({
					params : {
						start : 0,
						limit : 30
					}
				});
				if (result.success) {
					Ext.example.msg("提示", "操作成功");
				}
				me.close();
			},
			failure : function(form, action) {
				var result = action.result;
				me.store.load({
					params : {
						start : 0,
						limit : 30
					}
				});
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});