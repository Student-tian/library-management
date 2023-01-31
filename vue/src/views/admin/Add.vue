<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">新增管理员</div>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="姓名" prop="username">
        <el-input v-model="form.username" placeholder="请输入姓名"></el-input>
      </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="form.email" placeholder="请输入地址"></el-input>
              </el-form-item>
                <el-form-item label="联系方式" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
              </el-form-item>
    </el-form>
    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: 'Add',
  data(){

    const checkphone = (rule, value, callback) => {
      if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(value)) {
        callback(new Error('请输入合法的手机号'));
      }
      callback()
    };
    return {
      form: {
      },
      rules:{
        username:[
          {required:true,message:"请输入用户名",trigger:'blur'},
          {min:3, max:10,message:"长度为3-10个字符",trigger:'blur'},
        ],

        phone:[
          {validator:checkphone,trigger:'blur'}

        ]
      }
    }
  },
  methods:{
    save(){
      // this.$refs["ruleForm"].validate((valid)=>{
      //   if(valid){
          request.post('/admin/save',this.form).then(res =>{
            if(res.code==='200'){
              this.$notify.success("新增成功")
              this.$router.push("/adminList")
              // this.$refs['ruleForm'].resetFields();
            }else {
              this.$notify.error(res.msg)
            }
          })
        // }
      // })
    }
  }
}
</script>