<template>
  <a-spin :spinning="confirmLoading">
  <j-form-container :disabled="formDisabled">

      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">

        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name" label="标题">
                    <a-input placeholder="请输入标题" v-model="model.name" />
                  </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="leaveDay" label="请假天数">
                    <a-input-number v-model="model.leaveDay"/>
                  </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reason" label="请假理由">
                    <a-input placeholder="请输入请假理由" v-model="model.reason" />
                  </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="actStatus" label="审批流状态">
                    <a-input placeholder="请输入审批流状态" v-model="model.actStatus" />
                  </a-form-model-item>

      </a-form-model>
  </j-form-container>
  </a-spin>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import moment from "moment"

  export default {
    name: "ZhLeaveOaForm",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
                                                                                                                                                                     },
        url: {
          add: "/biz/zhLeaveOa/add",
          edit: "/biz/zhLeaveOa/edit",
          queryById: "/biz/zhLeaveOa/queryById"
        },
      }
    },
computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form
        .validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message
                .success(res.message);
                that.$emit('ok');
              }else{
                that.$message
                .warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
    }
  }
</script>

<style lang="less" scoped>

</style>