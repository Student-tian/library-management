<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">新增用户</div>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>

      <el-form-item label="年龄" prop="age">
        <el-input v-model.number="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <!--              <el-input v-model="form.sex" placeholder="请输入性别"></el-input>-->
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" placeholder="请输入地址"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
      <!--      <el-button type="danger">返回</el-button>-->
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: 'Add',
  data(){
    const checkAge = (rule, value, callback) => {
      if (!value) {
        return  callback(new Error('年龄不能为空'));
      }if (value>120||value<0) {
        callback(new Error('请输入合法年龄'));
      }
    };
    const checkphone = (rule, value, callback) => {
      if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(value)) {
        callback(new Error('请输入合法的手机号'));
      }
      callback()
    };
    return {
      form: {
        sex:'男'
      },
      rules:{
        name:[
          {required:true,message:"请输入姓名",trigger:'blur'},
        ],
        age:[
          {validator:checkAge,trigger:'blur'}
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
      request.post('/user/save',this.form).then(res =>{
        if(res.code==='200'){
          this.$notify.success("新增成功")
          this.$router.push("/user3List")
          // this.$refs['ruleForm'].resetFields();
          this.form={sex:'男'}
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