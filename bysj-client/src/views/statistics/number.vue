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
      report: []
    }
  },
  mounted() {
    this.$get(api.report.number).then(data => {
      this.report = data;
      this.showEcarts();
    }).catch(err => {
      this.loading = false;
      this.$message.error(err.message);
    });
  },
  methods: {
    showEcarts() {
      //饼图
      const mypie = this.$echarts.init(this.$refs.boxpie, 'vintage')
      mypie.setOption(
          {
            title: {
              text: '景点收藏分析',
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
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            legend: {
              bottom: 10,
              left: 'center'
            },
            series: [
              {
                name: "景点收藏分析",
                type: 'pie',
                radius: '65%',
                center: ['50%', '50%'],
                selectedMode: 'single',
                data: this.report,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
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


