<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">编辑分类信息</div>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="图书标准码"  prop="bookNo">
        <el-select v-model="form.bookNo" filterable placeholder="请选择" @change="selBook">
          <el-option
              v-for="item in books"
              :key="item.id"
              :label="item.bookNo"
              :value="item.bookNo">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" disabled></el-input>
      </el-form-item>
      <el-form-item label="所需积分"  prop="score">
        <el-input v-model="form.score" disabled></el-input>
      </el-form-item>

      <el-form-item label="图书数量"  prop="nums">
        <el-input v-model="form.nums" disabled></el-input>
      </el-form-item>
      <br/>

      <el-form-item label="会员码" prop="userNo">
        <el-select v-model="form.userNo" filterable placeholder="请选择" @change="selUser">
          <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.username"
              :value="item.username">
          </el-option>
        </el-select>

      </el-form-item>
      <el-form-item label="用户名称"  prop="userName">
        <el-input v-model="form.userName" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户联系方式" prop="userPhone">
        <el-input v-model="form.userPhone" disabled ></el-input>
      </el-form-item>
      <el-form-item label="用户账户积分" prop="account">
        <el-input v-model="form.account" disabled ></el-input>
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
  name: 'EditBorrow',
  data(){
    return {

      categories:[],
      form: {},
      books:[],
      users:[],
      rules:{
        bookNo:[
          {required:true,message:"请输入图书标准码",trigger:'blur'},
        ],
        userNo:[
          {required:true,message:"请输入会员码",trigger:'blur'},
        ]
      }
    }
  },
  created() {
      request.get('/book/list').then(res=>{
        this.books=res.data
      })
      request.get('/user/list').then(res=>{
        this.users=res.data.filter(v=>v.status)
      })
    const id=this.$route.query.id
    request.get("/borrow/"+id).then(res =>{
      this.form=res.data;
    })
  },
  methods:{
    save(){
      request.put('/borrow/update',this.form).then(res =>{
        if(res.code==='200'){
          this.$notify.success("修改成功")
          this.$router.push("/listBorrow")
        }else {
          this.$notify.error(res.msg)
        }
      })
    },
    selBook(){
      const book=this.books.find(v=>v.bookNo===this.form.bookNo)
      request.get('/book/'+book.id).then(res=>{
        this.form.bookName=res.data.name
        this.form.score=res.data.score
        this.form.nums=res.data.nums
      })
    }
    },
    selUser(){
      const user=this.users.find(v=>v.username===this.form.userNo)
      request.get('/user/'+user.id).then(res=>{
        this.form.userName=res.data.name
        this.form.userPhone=res.data.phone
        this.form.account=res.data.account
      })
    },
}
</script>