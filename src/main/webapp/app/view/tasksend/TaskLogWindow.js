Ext.ns('app.view.tasksend');
app.view.tasksend.TaskLogWindow = Ext.extend(Ext.Window, {
			title : '各个网点播放情况',
			modal : true,
			width : 1080,
			height : 500,
			layout : 'fit',
			taskId : '',
			initComponent : function() {
				var me = this;
				var panel = new app.view.tasksend.TaskLogGridPanel({
							taskId : me.taskId,
							taskSendId : me.taskSendId
						});
				me.items = [panel];
				app.view.tasksend.TaskLogWindow.superclass.initComponent.apply(
						this, arguments);
			}
		});