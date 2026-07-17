<template>
  <div style="display: flex; justify-content: space-around;">
    <div class="echart-box" ref="boxpie" style="width: 900px"></div>
  </div>
</template>
<script>

import api from "@/api";

export default {
  data() {
    return {
      name: [],
      value: []
    }
  },
  mounted() {
    this.$get(api.report.state).then(data => {
      console.log(data, "========")
      this.name = data.name;
      this.value = data.value;
      this.showEcarts();
    }).catch(err => {
      this.loading = false;
      this.$message.error(err.message);
    });

    // this.$http({
    //   url: "/report/number/data",
    //   method: "get"
    // }).then(({data}) => {
    //   this.name = data.name;
    //   this.value = data.value;
    //   this.showEcarts();
    // });
  },
  methods: {
    showEcarts() {
      //饼图
      const mypie = this.$echarts.init(this.$refs.boxpie, 'vintage')
      mypie.setOption(
          {
            title: {
              text: '线路统计',
              subtext: '报表统计',
              left: 'center'
            },
            toolbox: {
              show: true,
              feature: {
                restore: {show: true},
                saveAsImage: {show: true}
              }
            },
            calculable: true,
            xAxis: {
              type: 'category',
              data: this.name
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c}'
            },
            legend: {
              bottom: 10,
              left: 'center'
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: "线路统计",
                data: this.value,
                type: 'bar',
                markLine: {
                  data: [{type: 'average', name: 'Avg'}]
                }
              }
            ]
          }
      )
    },
  },
}
</script>
<style lang="scss" scoped>
.echart-box {
  width: 80vh;
  height: 75vh;
  margin: 20px;
}
</style>


