Ext.ns('app.view.system');
app.view.system.RollMassageWindow = Ext.extend(Ext.Window, {
	title : '编辑滚屏字幕',
	width : 490,
//	height : 400,
	buttonAlign : 'center',
	iconCls : 'create',
	modal : true,
	initComponent : function() {
		var me = this;
		
		Ext.Ajax.request({
			url : 'cn/wizool/bank/servlet/ConfigServlet?method=getRollMassage',
			success : function(response) {
				var text = Ext.decode(response.responseText);
				
				me.findByType('textarea')[0].setValue(text.message);
			}
		});
		me.items = [ {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
			defaults : {
				labelWidth : 40,
				anchor : '97%'
			},
			items : [{
				xtype : 'textarea',
				name : 'message',
				fieldLabel : '滚屏信息',
				preventScrollbars : true,
//				growMax : 300,
				maxLength : 450
			}]
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
		app.view.system.RollMassageWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/ConfigServlet?method=createRoll',
			method : 'POST',
			success : function(form, action) {
				var result = action.result;
				if (result) {
					if (result.success) {
						Ext.example.msg("提示", "操作成功");
						me.close();}
				} else {
					Ext.Msg.alert("提 示", result.message);
				}
			}
		});
	}
});