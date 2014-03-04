Ext.ns('app.view.tasklog');
app.view.tasklog.TaskMagGridPanel = Ext
		.extend(
				Ext.Panel,
				{
					checked : false,
					kill : true,
					html : '<div id="text" style="font-size: 25px; line-height: 40px; position: relative; left: 200px; width: 80%; height: 80px;"> <marquee id="moveText"  direction="up" onfinish="finishMove()" scrollamount="7" style="height: 500px;width:1000px;"> </marquee> </div>',
					initComponent : function() {
						var me = this;
						me.count = 0;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/TaskLogServlet?method=getMsgNotices"
											}),
									reader : new Ext.data.JsonReader(
											{
												totalProperty : "total",
												root : 'root',
												fields : [ 'taskId',
														'taskName', 'person',
														'birth', 'important' ]
											})
								});

						if (me.checked) {
							me
									.on(
											'dblclick',
											function() {
												var node = me
														.getSelectionModel().selections.items;
												window.location.href = 'app://'
														+ node[0].get('taskId');
												me.deleteMoveText(node[0]);
											});
						}
						me.store = store;
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});

						var task = {
							run : function() {
								me.store.load({
									params : {
										start : 0,
										limit : 25
									}
								});
							},
							interval : 60000
						}

						var task1 = {
							run : function() {
								if (me.store.getCount() > 0) {
									var vod = new Audio(
											'app\\resource\\music\\keep.mp3');
									vod.play();
								}
							},
							interval : 10000
						}

						var runner = new Ext.util.TaskRunner();
						runner.start(task);
						runner.start(task1);
						me.store
								.on(
										'load',
										function(store, records) {
											if (records.length > me.count) {
												var addNum = records.length
														- me.count;
												for ( var i = 0; i < addNum; i++) {
													var el = document
															.createElement("div");
													el
															.setAttribute(
																	'taskId',
																	records[i]
																			.get("taskId"));
													el
															.appendChild(document
																	.createTextNode(records[i]
																			.get('taskName')
																			+ "---"
																			+ records[i]
																					.get('person')
																			+ "---"
																			+ records[i]
																					.get("birth")));
													var moveEl = document
															.getElementById("moveText");
													var imp = records[i]
															.get("important");
													if (imp == "普通") {
														el.setAttribute(
																'class',
																'font1');
													} else if (imp == "重要") {
														el.setAttribute(
																'class',
																'font2');
													} else if (imp == "紧急") {
														el.setAttribute(
																'class',
																'font3');
													}
													el.setAttribute(
															'onmouseover',
															'stop()');
													el.setAttribute(
															'onmouseout',
															'start()');
													moveEl.appendChild(el);
													Ext
															.get(el)
															.on(
																	"click",
																	function() {
																		var taskId = records[i]
																				.get("taskId");
																		var node = records[i];
																		var me1 = me;
																		return function() {
																			window.location.href = 'app://'
																					+ taskId;
																			me1
																					.deleteMoveText(node);
																			window
																					.setTimeout(
																							function() {
																								me1.store
																										.load({
																											params : {
																												start : 0,
																												limit : 999999
																											}
																										});
																							},
																							5000);
																		}
																	}());
												}
											}

											var vod1 = new Audio(
													'app\\resource\\music\\Sent.wav');
											var vod2 = new Audio(
													'app\\resource\\music\\2.mp3');
											var vod3 = new Audio(
													'app\\resource\\music\\3.mp3');

											if (records.length - me.count == 1) {
												var imp = records[0]
														.get("important");
												if (imp == "普通") {
													vod1.play();
												} else if (imp == "重要") {
													vod2.play();
												} else if (imp == "紧急") {
													vod3.play();
												}
											} else if (records.length
													- me.count > 1) {
												var str = "";
												for ( var i = 0; i < records.length; i++) {
													str += records[i]
															.get('important');
												}
												if (str.indexOf("紧急") != -1) {
													vod1.play();
												} else if (str.indexOf("重要") != -1) {
													vod2.play();
												} else if (str.indexOf("普通") != -1) {
													vod3.play();
												}
											}

											me.records = records;
											me.count = records.length;
										});
						app.view.tasklog.TaskMagGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					deleteMoveText : function(node) {
						var moveText = document.getElementById("moveText");
						var elArr = moveText.childNodes;
						for ( var i = 1; i < elArr.length; i++) {
							if (node.get('taskId') == elArr[i]
									.getAttribute("taskId")) {
								moveText.removeChild(elArr[i]);
								return;
							}
						}
					}
				})