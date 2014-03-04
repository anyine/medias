Ext.ns('app.view.background');
app.view.background.FileShareEditWindow = Ext.extend(Ext.Window, {
	title : '新增文件',
	width : 600,
	buttonAlign : 'center',
	modal : true,
	num : 0,
	initComponent : function() {
		var me = this;
		var fileStore = new Ext.data.SimpleStore({
			fields : ['name', 'size', 'type', 'lastModifiedDate'],
			data : []
		});
		var panel = new Ext.grid.GridPanel({
			layout : 'fit',
			height : 300,
			store : fileStore,
			columns : [new Ext.grid.RowNumberer({
				width : 40,
				header : '序号'
			}), {
				header : '名称',
				dataIndex : 'name',
				width : 188
			}, {
				header : '大小(字节)',
				dataIndex : 'size',
				width : 90,
				align : 'right'
			}, {
				header : '类型',
				dataIndex : 'type',
				width : 80
			}, {
				header : '修改时间',
				dataIndex : 'lastModifiedDate',
				width : 120
			}]
		});

		me.items = [{
			xtype : 'form',
			fileUpload : true,
			bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
			defaults : {
				labelWidth : 40,
				allowBlank : false,
				anchor : '96%'
			},
			items : [{
				xtype : 'multifilesfieldVal',
				fieldLabel : '文件',
				name : 'name',
				blankText : '文件名称不能为空',
//				vtype:'fileType', 
//				vtypeText:'上传文件必须是xls,doc,txt,ppt类型文件中的一种!!!', 
//				fileTypes:['png','gif','jpg','jpeg','bmp'], 
				listeners : {
					change : me.change
				}
			}, panel]
		}];

		me.buttons = [{
			xtype : 'button',
			text : '保存',
			handler : me.save,
			scope : me
		}, {
			xtype : 'button',
			text : '取消',
			handler : function(button) {
				me.close();
			}
		}];
		app.view.background.FileShareEditWindow.superclass.initComponent.apply(
				this, arguments);
	},
	change : function(filelist) {
		var win = this.findParentByType();
		win.datas = [];
		var data = [];
		var fileStore = win.findByType('grid')[0].store;
		for (var i = 0; i < filelist.length; i++) {
			var files = filelist[i];
			if (files.length != 0) {
				for (var j = 0; j < files.length; j++) {
					var lastModifiedDate = files[j].lastModifiedDate;
					var date = lastModifiedDate.format('Y-m-d H:i:s');
					data = [files[j].name, files[j].size, files[j].type, date];
					win.datas.push(data);
					data = [];
				}
			}
		}
		fileStore.loadData(win.datas);
	},
	save : function(button) {
		var me = this;
		var win = button.findParentByType();
		var form = win.findByType('form')[0];
		var wait = new app.system.WaitWindow();
		wait.show();
		form.getForm().submit({
					url : 'cn/wizool/bank/servlet/DocumentServlet?method=uploadBackGround',
					method : 'POST',
					success : function(form, action) {
						var result = action.result;
						if (result) {
							if (result.success) {
								wait.close();
								Ext.example.msg("提示", "操作成功");
								win.store.load({
									params : {
										start : 0,
										limit : 25
									}
								});
								win.close();
							} else {
								wait.close();
								Ext.Msg.alert("提示", result.message);
							}
						}else{
							wait.close();
							Ext.Msg.alert("提示", "没有返回信息！");
						}
					},
					failure : function(form, action) {
						var result = action.result;
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});
						Ext.Msg.alert("提示", result.message);
						wait.close();
						win.close();
					}
				});
	}
});
