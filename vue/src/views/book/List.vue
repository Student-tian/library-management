<template>
  <div>
    <!--    搜索表单-->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px" placeholder="请输入图书名称" v-model="params.name"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入图书标准码" v-model="params.bookNo"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-search"></i>重置</el-button>
    </div>
    <el-table :data="tableData" stripe row-key="id" default-expand-all>
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="name" label="图书名称"></el-table-column>
      <el-table-column prop="description" width="140" label="描述"></el-table-column>
      <el-table-column prop="publishDate" label="发布日期"></el-table-column>
      <el-table-column prop="author" label="图书作者"></el-table-column>
      <el-table-column prop="publisher" label="出版社"></el-table-column>
      <el-table-column prop="category" label="图书分类"></el-table-column>
      <el-table-column prop="bookNo" label="图书标准码"></el-table-column>
      <el-table-column prop="score" label="借书积分"></el-table-column>
      <el-table-column prop="nums" label="数量"></el-table-column>
      <el-table-column prop="cover" label="封面">
        <template v-slot="scope">
          <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="更新时间"></el-table-column>

      <el-table-column label="操作" width="140">
        <template v-slot="scope">
<!--          <el-button type="success" v-if="!scope.row.pid" @click="handleAdd(scope.row)">添加二级分类</el-button>-->
          <el-button type="primary" @click="$router.push('/editBook?id='+scope.row.id)">编辑</el-button>
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
  name: 'ListBook',
  data(){
    return {
      admin:Cookies.get('admin')? JSON.parse(Cookies.get('admin')):{},
      tableData:[],
      total :0,

      params:{
        pageSize:10,
        pageNum:1,
        name:'',
        bookNo: ''
      }
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){

      request.get('/book/page',{
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
      request.delete("/book/delete/"+id).then(res=>{
        if(res.code==='200'){
          this.$notify.success("删除成功");
        }else{
          this.$notify.error(res.msg);
        }
        this.load()
      })
    },

  }
}
</script>
