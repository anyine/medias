Ext.ns('app.view.dept');
app.view.dept.MachineCheckWindow = Ext.extend(Ext.Window, {
	title : '机器选择',
	width : 600,
	height : 500,
	buttonAlign : 'center',
	ids : '',
	layout : 'fit',
	initComponent : function() {
		var me = this;
		var grid = new app.view.dept.MachineGridPanel({
			title : '',
			dbcheck : true
		});
		me.buttons = [ {
			text : '确定',
			handler : me.save,
			scope : me
		}, {
			text : '取消',
			handler : function() {
				me.close();
			}
		} ];
		me.items = [ grid ];
		app.view.dept.MachineCheckWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var items = me.items.items[0].selModel.selections.items;
		if (items.length != 0) {
			for ( var i = 0; i < items.length; i++) {
				me.ids += items[i].data.id + " ";
			}
			me.close();
		} else {
			Ext.Msg.alert('提示', '请选择一项');
		}
	}
});