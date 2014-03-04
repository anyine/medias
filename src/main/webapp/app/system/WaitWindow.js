Ext.ns("app.system");
app.system.WaitWindow = Ext.extend(Ext.Window, {
			title : '处理中......',
			modal : true,
			initComponent : function() {
				var me = this;

				var bar = new Ext.ProgressBar({
							width : 300
						});

				me.items = [bar];
				bar.wait({
							interval : 1000,
							duration : 6000000000000,
							increment : 15
						});

				app.system.WaitWindow.superclass.initComponent.apply(this,
						arguments);
			}
		});