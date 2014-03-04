Ext.ns('app.view.dept');
app.view.dept.DeptTreePanel = Ext.extend(Ext.tree.TreePanel, {
	title : '机构管理',
	autoScroll : true,
	containerScroll : true,
	checkModel : 'cascade',
	initComponent : function() {
		var me = this;
		var root = new Ext.tree.AsyncTreeNode({
			id : 'root',
			expanded : true
		});
		var typeSt = new Ext.data.SimpleStore({
			fields : [ 'id', 'name' ],
			data : [ [ '广告', '广告' ], [ '培训', '培训' ], [ '全选', '全选' ] ]
		});
		var combo = {
			xtype : 'combo',
			fieldLabel : '类型',
			store : typeSt,
			editable : false,
			triggerAction : 'all',
			valueField : 'name',
			mode : 'local',
			displayField : 'name',
			name : 'type',
			width : 80,
			listeners : {
				select : function(a, b) {
					var s = b.get("name");
					if (s == '培训') {
						me.type = '培训';
					} else if (s == '广告') {
						me.type = '广告';
					} else if (s == '全选') {
						me.type = '';
					}
					root.reload();
				}
			}
		};
		if (me.classify == 'machine') {
			// 机器列表左侧显示的机构树，展开所有节点，无选择框
			me.loader = new Ext.tree.TreeLoader({
				dataUrl : 'cn/wizool/bank/servlet/DepartmentServlet',
				baseParams : {
					method : 'getDepts'
				}
			});
		} else if (me.classify == 'taskSelectMachine') {
			// 编辑任务时，左侧显示的机构树，展开所有节点，有选择框
			me.loader = new Ext.tree.TreeLoader({
				dataUrl : 'cn/wizool/bank/servlet/DepartmentServlet',
				baseAttrs : {
					uiProvider : Ext.ux.TreeCheckNodeUI
				},
				baseParams : {
					method : 'get',
					style : 'machinelist',
					deptIds : me.deptIds
				}
			});
			me.loader.on("beforeload", function(treeLoader, node) {
				Ext.apply(treeLoader.baseParams, {
					type : me.type == null ? '' : me.type,
					deptIds : me.deptIds == null ? '' : me.deptIds
				});
			});
			me.tbar = [ {
				xtype : 'label',
				text : '类型：'
			}, combo ]
		} else {
			// 机构管理中显示的机构树，只展开到办事处底下一节节点
			me.loader = new Ext.tree.TreeLoader({
				dataUrl : 'cn/wizool/bank/servlet/DepartmentServlet',
				baseParams : {
					method : 'get',
					style : 'branchlist'
				}
			});
			me.tbar = [ '->', '-', {
				text : '新增顶层机构',
				iconCls : 'create',
				handler : me.createRoot,
				scope : me,
				hidden : me.dbcheck
			} ];
			me.on('contextmenu', me.contextmenu);
		}
		me.root = root;
		me.rootVisible = false;
		app.view.dept.DeptTreePanel.superclass.initComponent.apply(this,
				arguments);
	},
	createRoot : function() {
		var me = this;
		var win = new app.view.dept.DeptEditWindow({
			title : '新增顶层机构',
			iconCls : 'create',
			root : me.root,
			type : 'createRoot',
			method : 'createBranch'
		});
		win.show();
	},
	contextmenu : function(node, e) {
		var me = this;
		// 点击右键出现tree菜单
		node.select();
		// 点击右键同时选中该项
		e.preventDefault();
		var treeMenu = new Ext.menu.Menu([ {
			text : '新增下级机构',
			iconCls : 'create',
			pressed : true,
			handler : me.create,
			scope : me
		}, {
			text : '修改',
			iconCls : 'modify',
			pressed : true,
			handler : me.modify,
			scope : me
		}, {
			text : '删除',
			iconCls : 'delete',
			pressed : true,
			handler : me.remove,
			scope : me
		} ]);
		// 定位菜单的显示位置
		treeMenu.showAt(e.getPoint());
	},
	create : function(button) {
		var me = this;
		var node = this.getSelectionModel().getSelectedNode();
		var win = new app.view.dept.DeptEditWindow({
			title : '新增下级机构',
			iconCls : 'create',
			modelId : node.id,
			root : me.root,
			type : 'createX',
			method : 'createBranch'
		});
		win.show();
	},
	modify : function() {
		var me = this;
		var node = this.getSelectionModel().getSelectedNode();
		var win = new app.view.dept.DeptEditWindow({
			title : '修改机构名称',
			iconCls : 'modify',
			modelId : node.id,
			root : me.root,
			method : 'modifyBranch'
		});
		win.show();
		win.findByType('textfield')[0].setValue(node.text)
	},
	remove : function() {
		var me = this;
		var node = this.getSelectionModel().getSelectedNode();
		if (node == null) {
			Ext.Msg.alert('提示', '请选择一项');
		} else {
			Ext.MessageBox.confirm('提示', '确定要删除此项?', function(btn) {
				if (btn == 'yes') {
					Ext.Ajax.request({
						url : 'cn/wizool/bank/servlet/DepartmentServlet',
						params : {
							method : 'deleteBranch',
							id : node.id
						},
						success : function(result) {
							var obj = Ext.decode(result.responseText);
							if (obj.success) {
								Ext.example.msg("提示", "操作成功");
								me.root.reload();
							} else {
								Ext.Msg.alert('提示', obj.message);
								me.root.reload();
							}
						}
					});
				}
			});
		}
	}
});
