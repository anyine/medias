Ext.ns('app.view.dept');
app.view.dept.BranchCheckWindow = Ext.extend(Ext.Window, {
	title : '机构选择',
	width : 500,
	height : 500,
	buttonAlign : 'center',
	id : '',
	name : '',
	layout : 'fit',
	initComponent : function() {
		var me = this;
		var tree = new app.view.dept.DeptTreePanel({
			title : '',
			dbcheck : true,
			checkModel : 'cascade',
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'cn/wizool/bank/servlet/DepartmentServlet',
				baseParams : {
					method : 'get',
					style : 'branchlist'
				}
			})
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
		me.items = [ tree ];
		app.view.dept.BranchCheckWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var model = me.items.items[0].getSelectionModel().getSelectedNode();
		me.id = model.id;
		me.name = model.text;
		me.close();
	}
});