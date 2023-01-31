<template>
  <div>
    <!--    搜索表单-->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px" placeholder="请输入图书名称" v-model="params.name"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入图书标准码" v-model="params.bookNo"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入用户名称" v-model="params.userName"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-search"></i>重置</el-button>
    </div>
    <el-table :data="tableData" stripe row-key="id" default-expand-all>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="bookName" label="图书名称"></el-table-column>
      <el-table-column prop="bookNo"  label="图书标准码"></el-table-column>
      <el-table-column prop="userNo" label="会员码"></el-table-column>
      <el-table-column prop="userName" label="用户姓名"></el-table-column>
      <el-table-column prop="userPhone" label="联系方式"></el-table-column>
      <el-table-column prop="score" label="所用积分"></el-table-column>
      <el-table-column prop="status" label="借出状态"></el-table-column>
      <el-table-column prop="days" label="借出天数"></el-table-column>
      <el-table-column prop="returnDate" label="归还日期"></el-table-column>
      <el-table-column prop="createtime" label="借出时间"></el-table-column>
      <el-table-column prop="realDate" label="实际归还日期"></el-table-column>
      <el-table-column label="操作" >
        <template v-slot="scope">
        <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
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
  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'List',
  data(){
    return {
      admin:Cookies.get('admin')? JSON.parse(Cookies.get('admin')):{},
      tableData:[
          // realDate,
      ],
      total :0,

      params:{
        pageSize:10,
        pageNum:1,
        name:'',
        bookNo: '',
        userName:''
      }
    }
  },
  created() {
    this.load()
    console.log(this.tableData)
  },
  methods:{
    load(){

      request.get('/borrow/pageRetur',{
        params: this.params
      }).then(res =>{
        this.tableData=res.data.list
        this.total=res.data.total
      })
    },
    reset(){
      this.params={
        pageSize:10,
        pageNum:1,
        name:'',
        bookNo:''
      }
      this.load()
    },
    handleCurrentChange(pageNum){
      this.params.pageNum=pageNum
      this.load()
    },
    del(id){
      request.delete("/borrow/deleteRetur/"+id).then(res=>{
        if(res.code==='200'){
          this.$notify.success("删除成功");
        }else{
          this.$notify.error(res.msg);
        }
        this.load()
      })
    },
    returnBooks(row){

    }
  }
}
</script>
