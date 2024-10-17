<template>
  <div>
    <iframe
      :src="iframUrl"
      frameborder="0"
      width="100%"
      height="800px"
      scrolling="auto"
      style="background-color: #fff;"
    ></iframe>
  </div>
</template>

<script>
import { filterObj } from '@/utils/util'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JEllipsis from '@/components/jeecg/JEllipsis'
import { deleteAction, getAction, downFile } from '@/api/manage'

export default {
  name: 'processDesign',
  mixins: [JeecgListMixin],
  components: {
    JEllipsis
  },
  data() {
    return {
      /*流程设计器连接*/
      iframUrl: `${window._CONFIG['domianURL']}/bpm/index.html`,
      /*新增流程框参数*/
      createObj: {
        visible: false,
        confirmLoading: false
      },
      /*设计流程框参数*/
      updateObj: {
        visible: false,
        confirmLoading: false
      },

      description: '这是流程模型列表页面',
      // 查询条件
      queryParam: {
        createTimeRange: [],
        keyWord: ''
      },
      tabKey: '1',
      // 表头
      columns: [
        {
          title: '#',
          width: 50,
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '模型id',
          width: 150,
          align: 'center',
          dataIndex: 'id'
        },
        {
          title: '模型名称',
          width: 150,
          align: 'center',
          dataIndex: 'name',
          scopedSlots: { customRender: 'logContent' }
        },
        {
          title: '模型key',
          width: 150,
          dataIndex: 'key',
          align: 'center'
        },
        {
          title: '版本',
          width: 80,
          dataIndex: 'revision',
          align: 'center',
          scopedSlots: { customRender: 'revision' }
        },
        {
          title: '备注描述',
          width: 150,
          dataIndex: 'metaInfo',
          align: 'center',
          scopedSlots: { customRender: 'metaInfo' }
        },
        {
          title: '创建时间',
          width: 150,
          dataIndex: 'createTime',
          align: 'center'
          // sorter:true
        },
        {
          title: '最后更新时间',
          width: 150,
          dataIndex: 'lastUpdateTime',
          align: 'center'
        },
        {
          title: '操作',
          width: 250,
          dataIndex: '',
          scopedSlots: { customRender: 'make' },
          align: 'center'
        }
      ],
      labelCol: {
        xs: { span: 1 },
        sm: { span: 2 }
      },
      wrapperCol: {
        xs: { span: 10 },
        sm: { span: 16 }
      },
      url: {
        list: '/activiti/models/modelListData',
        delete: '/activiti/models/delete/',
        deployment: '/activiti/models/deployment/',
        create: '/bpm/index.html',
        update: '/bpm/index.html'
      }
    }
  },
  methods: {
    /*创建流程*/
    createObjOk(e) {
      this.createObj.confirmLoading = true
      this.updateObj.visible = true
      this.iframUrl = `${window._CONFIG['domianURL']}${this.url.create}`
      this.createObj.visible = false
      this.createObj.confirmLoading = false
    },
    /*修改流程*/
    updatelc(id) {
      var _this = this
      this.$message.loading('稍等。。。', 0.8).then(() => {
        _this.createObj.confirmLoading = true
        _this.iframUrl = `${window._CONFIG['domianURL']}${_this.url.update}`
        _this.updateObj.visible = true
        _this.createObj.confirmLoading = false
      })
    },
    cancelUpdate() {
      var _this = this
      this.$confirm({
        title: '关闭前请确认已保存修改?',
        content: '关闭后未保存的修改将丢失！',
        okText: '确认关闭',
        okType: 'danger',
        cancelText: '再看看',
        onOk() {
          _this.updateObj.visible = false
          _this.loadData()
        },
        onCancel() {
          console.log('Cancel')
        }
      })
    },
    /*发布流程*/
    deployment(row) {
      var _this = this
      var id = row.id
      var name = row.name
      this.$confirm({
        title: '确认部署流程?',
        content: `确认部署流程：${name}`,
        onOk() {
          getAction(_this.url.deployment + id).then(res => {
            if (res.success) {
              _this.$message.success(res.message)
            } else {
              _this.$message.error(res.message)
            }
            this.loadData()
          })
        },
        onCancel() {}
      })
    },
    /*删除模型*/
    deletelc(y, row) {
      console.log(y, row)
      if (y) {
        getAction(this.url.delete + row.id).then(res => {
          if (res.success) {
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
          this.loadData()
        })
      }
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field
        this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination = pagination
      // this.loadData();
    },
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      this.loading = true
      getAction(this.url.list, params).then(res => {
        if (res.success) {
          let records = res.result || []
          this.dataSource = records
          this.ipagination.total = records.length
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false
      })
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam, this.isorter)
      delete param.createTimeRange // 时间参数不传递后台
      return filterObj(param)
    },

    // 重置
    searchReset() {
      var that = this
      var logType = that.queryParam.logType
      that.queryParam = {} //清空查询区域参数
      that.queryParam.logType = logType
      that.loadData(this.ipagination.current)
    },
    onDateChange: function(value, dateString) {
      console.log(dateString[0], dateString[1])
      this.queryParam.createTime_begin = dateString[0]
      this.queryParam.createTime_end = dateString[1]
    },
    onDateOk(value) {
      console.log(value)
    }
  },
  computed: {
    innerHeight: function() {
      return window.innerHeight
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
