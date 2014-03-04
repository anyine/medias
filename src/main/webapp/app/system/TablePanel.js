Ext.ns("app.system");
app.system.TablePanel = Ext.extend(Ext.TabPanel, {
	activeTab : 0,
	initComponent : function() {
		var me = this;
		// var obj = new App.view.type.TypePanel();
		me.items = [{
			xtype : 'panel',
			title : '首页',
			html : '<img src="app/resource/image/backgroundfirst.jpg" style="height:100%;width:100%"></img>'
		}]

		app.system.TablePanel.superclass.initComponent.apply(this, arguments);
	}
});