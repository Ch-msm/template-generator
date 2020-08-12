/**
 * 巡检:基础配置
 * @type {{}}
 */
const Config = {
  cTable: {},
  /**
   *初始化
   */
  init() {
    C.initUi();
    let search = {
      keyword: ''
    };
    //按钮初始化
    {
      $('#addButton').on('click', () => {
        Config.add();
      });
      $('#exportAllButton').on('click', () => {
      });
      $('#exportCheckedButton').on('click', () => {
      });
      $('[data-ui-button-search-now]').on('click', () => {
        Config.find(search);
      });
    }
    //关键字搜索项
    {
      let searchInput = document.getElementById('searchInput');
      searchInput.addEventListener('input', function (evt) {
        let me = evt.target;
        let word = me.value;
        console.debug('关键词', word);
        search.keyword = word;
      });
      searchInput.addEventListener('keydown', function (evt) {
        if (evt.key === 'Enter') {
          Config.find(search);
        }
      })
    }
    //切换控件
    {
      let toggle1 = new CUiButtonToggle(
          document.getElementById('toggle1'),
          new Map([['1','1'],['2','2'],['3','3']])
      );
      toggle1.addListener(array => {
        search['type2'] = array;
        Config.find(search);
      });
      let toggle2 = new CUiButtonToggle(
          document.getElementById('toggle2'),
          new Map([['4','4'],['5','5'],['6','6']])
      );
      toggle2.addListener(array => {
        search['type1'] = array;
        Config.find(search);
      });
    }
    Config.find(search);
  },
  /**
   *新增
   */
  add() {
    let table = this.table({}, false);
    let dialog = new CDialog("新增");
    dialog.setContent(table);
    dialog.addAction('保存', function () {
      let v = Tool.formValidator(table);
      if (v) {
        Config.API('add', v, () => {
          dialog.destroy();
          window.location.reload();
        });
      }
    });
    dialog.addAction('关闭', function () {
      dialog.destroy();
    }, true);
  },
  /**
   * 删除
   * @param id
   */
  del(id) {
    let uiTipDialog = new CTipDialog('提示', '确定删除？');
    uiTipDialog.addAction('确定', () => {
      Config.API('delete', {id: id}, () => {
        window.location.reload();
      });
    });
    uiTipDialog.addAction('取消', () => {
      uiTipDialog.destroy();
    }, true);
  },
  /**
   *修改
   */
  update(data) {
    let table = this.table(data, false);
    let dialog = new CDialog("修改");
    dialog.setContent(table);
    dialog.addAction('保存', function () {
      if (v) {
        v.id = data.id;
        Config.API('update', v, () => {
          dialog.destroy();
          window.location.reload();
        });
      }
    });
    dialog.addAction('关闭', function () {
      dialog.destroy();
    }, true);
  },
  /**
   * 查询
   * @param search
   */
  find(search) {
    console.log('重新加载列表', search);
    let listContainer = document.getElementById('list');
    listContainer.innerHTML = '';
    let dataCount = 0;
    Config.cTable = new CTable(
        listContainer,
        ['问题等级', '备注'],
        {
          check: false,
          checkMultiple: false,
          no: true,
          action: true,
          dataCountElement: document.body.querySelector('.ui-list-count'),
          moreCallback: currentCount => {
            //加载数据并显示
            searchList(currentCount + 1);
          }
        }
    );
    //显示数据
    let searchList = function (start) {
      let appendList = function (data) {
        let actionArray = [];
        let viewAction = C_HTML_TO_ELEMENT('<i class="iconfont icon-chakan" title="查看"></i>');
        viewAction.addEventListener('click', () => {
          Config.view(data);
        });
        actionArray.push(viewAction);
        let editAction = C_HTML_TO_ELEMENT('<i class="iconfont icon-bianji" title="修改"></i>');
        editAction.addEventListener('click', () => {
          Config.update(data);
        });
        actionArray.push(editAction);
        Config.cTable.append(
            data.id,
            (array => {
              return array.map(o => data[o])
            })(['level','remark']),
            actionArray,
            dataCount
        );
      };
      search.page = start;
      search.count = C.page_count;
      let uiTipWorking = new CTipWorking('正在加载数据');
      Config.API('find', search, data => {
        dataCount = data.count;
        for (let o of data.infos) {
          appendList(o);
        }
        uiTipWorking.destroy();
      });
    };
    searchList(1);
  },
  /**
   * 详情
   */
  view(data) {
    let table = this.table(data, true);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        table(data, true);
    let dialog = new CDialog("详情");
    dialog.setContent(table);
    dialog.addAction('关闭', function () {
      dialog.destroy();
    }, true);
  },
  /**
   * 导出
   */
  export(ids) {
    let uiTipWorking = new CTipWorking('正在导出数据');
    this.API('find', {ids: ids, type: this.type}, data => {
      let dataList = data.infos.map(o => (array => {
        return array.map(s => o[s]);
      })(['level','remark']));
      let array = {
        titleList:['问题等级', '备注'],
        dataList: dataList
      };
      NP.Poi.Excel.post(array, function (id) {
        NP.File.download(id);
        uiTipWorking.destroy();
      });
    });
  },
  /**
   * 表单
   */
  table(data, IsView) {
    let table = $('#table').clone();
    Object.keys(data).forEach(o => {
      $(`[data-name="${o}"]`, table).val(data[o]);
    });
    if (IsView) {
      table.addClass('tableForm-readonly');
    }
    table.show();
    return table;
  },
  /**
   * 后台交互
   */
  API(method, data, callback) {
    NP.Common.Action.post(data, "patrol/config/" + method, callback)
  }
};
