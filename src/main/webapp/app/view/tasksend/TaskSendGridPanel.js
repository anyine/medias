Ext.ns('app.view.tasksend');
app.view.tasksend.TaskSendGridPanel = Ext.extend(Ext.grid.GridPanel, {
	type : '',
	initComponent : function() {
		var me = this;
		var sm = new Ext.grid.CheckboxSelectionModel();
		me.sm = sm;
		var store = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						url : "cn/wizool/bank/servlet/TaskSendServlet?method=get"
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : "total",
						root : 'root',
						fields : ['id', 'schedule', 'birth', 'task',
								'taskName', 'deptIds', 'enable']
					})
		});

		me.tbar = ['->', {
					xtype : 'button',
					text : '查看各个网点播放情况',
					iconCls : 'view',
					handler : me.select,
					scope : me
				}, "-"];

		if (me.type == "medium") {
			me.tbar.push({
						xtype : 'button',
						text : '修改',
						iconCls : 'modify',
						handler : me.modify,
						scope : me
					});
		}
		me.columns = [new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), sm, {
					header : '任务调度方式',
					width : 200,
					dataIndex : 'schedule'
				}, {
					header : '调度开关',
					width : 60,
					dataIndex : 'enable'
				}, {
					header : '任务创建时间',
					width : 200,
					dataIndex : 'birth'
				}, {
					header : '任务名称',
					width : 200,
					dataIndex : 'taskName'
				}];
		me.store = store;

		me.store.on('beforeload', function(thiz) {
					Ext.apply(thiz.baseParams, {
								type : me.type
							});
				});
		me.store.load({
					params : {
						start : 0,
						limit : 25
					}
				});

		Ext.applyIf(me, {
					bbar : new Ext.PagingToolbar({
								store : me.store,
								pageSize : 25,
								displayInfo : false,
								emptyMsg : "",
								prevText : "上一页",
								nextText : "下一页",
								refreshText : "刷新",
								lastText : "最后页",
								firstText : "第一页",
								beforePageText : "当前页",
								displayInfo : true,
								displayMsg : '第{0} 到 {1} 条，共 {2}条',
								afterPageText : "共{0}页"
							})
				});

		app.view.tasksend.TaskSendGridPanel.superclass.initComponent.apply(
				this, arguments);
	},
	select : function() {
		var me = this;
		var node = this.getSelectionModel().selections.items;
		if (node.length == 1) {
			var win = new app.view.tasksend.TaskLogWindow({
						taskId : node[0].get('task'),
						taskSendId : node[0].get('id')
					});
			win.show();
		} else {
			Ext.Msg.alert('提示', '请选择一项');
		}

	},
	create : function() {
		var win = new app.view.tasksend.TaskSendEditWindow({
					thisStore : this.store
				});
		win.show();
	},
	modify : function() {
		var me = this;
		var node = this.getSelectionModel().selections.items;
		if (node.length == 1) {
			var win = new app.view.tasksend.TaskSendEditWindow2({
						type : me.type,
						thisStore : me.store,
						thisId : node[0].get("id")
					});
			win.show();
			win.findByType('textfield')[0].setValue(node[0].get("schedule"));
			win.findByType('hidden')[0].setValue(node[0].get("enable"));
			win.findByType('combo')[0].setValue(node[0].get("enable"));
			// if (node[0].get("enable") == true) {
			// win.findByType('combo')[0].setValue("开");
			// } else if (node[0].get("enable") == false) {
			// win.findByType('combo')[0].setValue("关");
			// }
		} else {
			Ext.Msg.alert('提示', '请选择一项');
		}
	},
	remove : function() {
		var me = this;
		var node = this.getSelectionModel().selections.items;
		if (node.length > 0) {
			Ext.Ajax.request({
						url : 'cn/wizool/bank/servlet/TaskSendServlet?method=delete',
						params : {
							id : me.getIds(node)
						},
						success : function(result) {
							var obj = Ext.decode(result.responseText);
							me.store.load({
										params : {
											start : 0,
											limit : 25
										}
									});
							Ext.Msg.alert("提示", "操作成功");
						}
					});
		} else {
			Ext.Msg.alert('提示', '请选择一项');
		}
	},
	getIds : function(model) {
		var ids = '';
		for (var i = 0; i < model.length; i++) {
			ids += model[i].json.id + " ";
		}
		return ids;
	}
});