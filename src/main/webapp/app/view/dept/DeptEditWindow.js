Ext.ns('app.view.dept');
app.view.dept.DeptEditWindow = Ext.extend(Ext.Window, {
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
				fieldLabel : '名称',
				name : 'name'
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
		app.view.dept.DeptEditWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/DepartmentServlet',
			params : {
				id : me.modelId,
				type : me.type,
				method : me.method
			},
			success : function(form, action) {
				var result = action.result;
				if (result.success) {
					Ext.example.msg("提示", "操作成功");
				}
				me.root.reload();
				me.close();
			},
			failure : function(form, action) {
				var result = action.result;
				me.root.reload();
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});