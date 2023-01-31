<template>
  <div>
    <!--    搜索表单-->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px" placeholder="请输入名称" v-model="params.name"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-search"></i>重置</el-button>
    </div>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="username" label="会员号"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="account" label=账户积分></el-table-column>
      <el-table-column label="状态" width="230">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeStatus(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template v-slot="scope">
          <el-button type="warning" @click="handleAccount(scope.row)">充值</el-button>
          <el-button type="primary" @click="$router.push('/editUser?id='+scope.row.id)">编辑</el-button>
          <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
<!--            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :page-size="params.pageSize"
          :current-page="params.pageNum"
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>
    <el-dialog title="充值" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="当前账户积分"  prop="account">
          <el-input v-model="form.account" disabled autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="积分"  prop="score">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAccount">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";

export default {
  name: 'User',
  data(){
    const checkNums=(rule,value,callback)=>{
      value=parseInt(value)
      // if(value===null||value===undefined){
      //   callback(new Error('请输入数字'))
      // }
      if(value>200||value<10){
        callback(new Error('请输入大于10小于200的数字'))
      }
    }
    return {
      tableData:[],
      total :0,
      params:{
        pageSize:10,
        pageNum:1,
        name:'',
        phone:'',
      },
      dialogFormVisible:false,
      form:{},
      rules:{
        score:[
          {required:true,message:"请输入积分",trigger:'blur'},
          {validator:checkNums,trigger:'blur'},
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods:{
    changeStatus(row){
      request.put('/user/update',row).then(res =>{
        if(res.code==='200'){
          this.$notify.success("更新成功")
          this.load()
        }else {
          this.$notify.error(res.msg)
        }
      })
    },
    load(){

      request.get('/user/page',{
        params: this.params
      }).then(res =>{
        this.tableData=res.data.list
        this.total=res.data.total
      })

      // fetch('http://localhost:9090/user/list').then(res=>res.json()).then(res=>{
      //   this.tableData=res.data;
      // })
    },
    reset(){
      this.params={
        pageSize:10,
        pageNum:1,
        name:'',
        phone:''
      }
      this.load()
    },
    handleCurrentChange(pageNum){
      this.params.pageNum=pageNum
      this.load()
    },
    del(id){
      request.delete("/user/delete/"+id).then(res=>{
        if(res.code==='200'){
          this.$notify.success("删除成功");
        }else{
          this.$notify.error(res.msg);
        }
        this.load()
      })
    },
    addAccount(){
      // this.$refs['formRef'].validate((valid)=>{
           // if(valid){
             request.post('/user/account',this.form).then(res=>{
               if(res.code==='200'){
                 this.$notify.success("充值成功")
                 this.dialogFormVisible=false
                 this.load()
                 // this.$router.push("/listBook")
                 // this.$refs['ruleForm'].resetFields();
               }else {
                 this.$notify.error(res.msg)
               }
             })
           // }
      // })
    },
    handleAccount(row){
      this.form=JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true
    }
  }
}
</script>
