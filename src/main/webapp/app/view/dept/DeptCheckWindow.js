Ext.ns('app.view.dept');
app.view.dept.DeptCheckWindow = Ext.extend(Ext.Window, {
	title : '机构选择',
	width : 500,
	height : 500,
	buttonAlign : 'center',
	ids : '',
	names : '',
	layout : 'fit',
	initComponent : function() {
		var me = this;
		var tree = new app.view.dept.DeptTreePanel({
			title : '',
			dbcheck : true,
			classify : me.classify == null ? '' : me.classify,
			// idsSelected : me.idsSelected, 已经选择的树节点，在修改点开的时候
			// 需要显示，以空格间隔的Id字符串
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'cn/wizool/bank/servlet/DepartmentServlet',
				baseAttrs : {
					uiProvider : Ext.ux.TreeCheckNodeUI
				},
				baseParams : {
					method : 'get',
					style : me.style == null ? 'branchlist' : me.style
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
		} ]
		me.items = [ tree ];

		app.view.dept.DeptCheckWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var model = me.items.items[0].getChecked();
		for ( var i = 0; i < model.length; i++) {
			me.ids += model[i].id + ' ';
			me.names += model[i].text + ' ';
		}
		me.close();
	}
});