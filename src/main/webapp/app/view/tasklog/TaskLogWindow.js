Ext.ns('app.view.tasklog');
app.view.tasklog.TaskLogWindow = Ext.extend(Ext.Window, {
	width : 1060,
	height : 600,
	layout : 'fit',
	initComponent : function() {
		var me = this;
		var noticeShow = new app.view.tasklog.TaskMagGridPanel({
			checked : true,
			kill : false,
			title : '新通知'
		});
		var fileShow = new app.view.tasklog.TaskLogGridPanel({
			checked : true,
			kill : false,
			title : '通知公告历史'
		});
		me.items = [ {
			xtype : 'tabpanel',
			activeTab : 0,
			items : [ noticeShow, fileShow ]
		} ];

		app.view.tasklog.TaskLogWindow.superclass.initComponent.apply(this,
				arguments);
	}
});