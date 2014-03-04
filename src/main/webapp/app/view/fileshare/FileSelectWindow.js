Ext.ns('app.view.fileshare');
app.view.fileshare.FileSelectWindow = Ext.extend(Ext.Window, {
			width : 500,
			height : 500,
			layout : 'fit',
			buttonAlign : 'center',
			modal : true,
			initComponent : function() {
				var me = this;
				var panel = new app.view.fileshare.FileSelectGridPanel({
							thisStore : me.thisStore
						});
				me.items = [panel];
				app.view.fileshare.FileSelectWindow.superclass.initComponent
						.apply(this, arguments);
			}
		});