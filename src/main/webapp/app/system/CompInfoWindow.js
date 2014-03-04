Ext.ns('app.view.system');
app.view.system.CompInfoWindow = Ext.extend(Ext.Window, {
	title : '机器详细列表',
	layout : 'fit',
	buttonAlign : 'center',
	maximizable : true,
	modal : true,
	initComponent : function() {
		var me = this;
		var taskInfo = new app.view.system.ComTasksGridPanel({
			title : '任务列表',
			deptId : me.deptId
		});
		var docInfo = new app.view.system.CompDocsGridPanel({
			title : '文件列表',
			deptId : me.deptId
		});
		me.items = [ {
			xtype : 'tabpanel',
			activeTab : 0,
			items : [ taskInfo, docInfo ]
		} ];
		app.view.system.CompInfoWindow.superclass.initComponent.apply(this,
				arguments);
	}
});