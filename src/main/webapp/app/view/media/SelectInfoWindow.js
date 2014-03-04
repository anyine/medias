Ext.ns('app.view.media');
app.view.media.SelectInfoWindow = Ext.extend(Ext.Window, {
	title : '任务详细列表',
	layout : 'fit',
	buttonAlign : 'center',
	maximizable : true,
	minimizable : true,
	modal : true,
	initComponent : function() {
		var me = this;
		var depInfo = new app.view.media.SelectDeptsGridPanel({
			title : '机构列表',
			taskId : me.taskId
		});
		var docInfo = new app.view.media.SelectDocsGridPanel({
			title : '文件列表',
			taskId : me.taskId
		});
		me.items = [ {
			xtype : 'tabpanel',
			activeTab : 0,
			items : [ docInfo, depInfo ]
		} ];
		app.view.media.SelectInfoWindow.superclass.initComponent.apply(this,
				arguments);
	}
});