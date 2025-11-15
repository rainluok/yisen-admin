<template>
  <div class="score-panel">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>{{ title }}</span>
          <el-button v-if="showRefresh" type="text" @click="handleRefresh"> 刷新 </el-button>
        </div>
      </template>

      <div class="score-content">
        <!-- 总分 -->
        <div class="total-score">
          <div class="score-number">{{ totalScore }}</div>
          <div class="score-label">总分</div>
        </div>

        <!-- 分数详情 -->
        <el-divider />

        <div class="score-details">
          <div v-for="item in scoreItems" :key="item.label" class="score-item">
            <div class="item-label">
              <el-icon v-if="item.icon">
                <component :is="item.icon" />
              </el-icon>
              <span>{{ item.label }}</span>
            </div>
            <div class="item-value" :style="{ color: item.color }">
              {{ item.value }}
            </div>
          </div>
        </div>

        <!-- 进度条 -->
        <el-divider />

        <div class="score-progress">
          <div class="progress-label">
            <span>完成进度</span>
            <span>{{ progress }}%</span>
          </div>
          <el-progress :percentage="progress" :color="progressColor" :show-text="false" />
        </div>

        <!-- 趋势图 -->
        <div v-if="showTrend" class="score-trend">
          <el-divider />
          <div class="trend-label">趋势</div>
          <div class="trend-chart">
            <div v-for="(point, index) in trendData" :key="index" class="trend-point" :style="{ height: `${point}%` }" />
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
  import { computed } from 'vue';

  const props = defineProps({
    title: {
      type: String,
      default: '积分面板',
    },
    totalScore: {
      type: Number,
      default: 0,
    },
    scoreItems: {
      type: Array,
      default: () => [],
    },
    progress: {
      type: Number,
      default: 0,
    },
    trendData: {
      type: Array,
      default: () => [],
    },
    showRefresh: {
      type: Boolean,
      default: true,
    },
    showTrend: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['refresh']);

  // 进度条颜色
  const progressColor = computed(() => {
    if (props.progress < 30) return '#f56c6c';
    if (props.progress < 70) return '#e6a23c';
    return '#67c23a';
  });

  // 刷新
  const handleRefresh = () => {
    emit('refresh');
  };
</script>

<style scoped lang="scss">
  .score-panel {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: bold;
    }

    .score-content {
      .total-score {
        text-align: center;
        padding: 20px 0;

        .score-number {
          font-size: 48px;
          font-weight: bold;
          color: #409eff;
          line-height: 1;
          margin-bottom: 8px;
        }

        .score-label {
          font-size: 14px;
          color: #909399;
        }
      }

      .score-details {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .score-item {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .item-label {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            color: #606266;

            .el-icon {
              font-size: 18px;
            }
          }

          .item-value {
            font-size: 18px;
            font-weight: bold;
          }
        }
      }

      .score-progress {
        .progress-label {
          display: flex;
          justify-content: space-between;
          margin-bottom: 8px;
          font-size: 14px;
          color: #606266;
        }
      }

      .score-trend {
        .trend-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 12px;
        }

        .trend-chart {
          display: flex;
          align-items: flex-end;
          justify-content: space-between;
          height: 80px;
          gap: 4px;

          .trend-point {
            flex: 1;
            background: linear-gradient(to top, #409eff, #66b1ff);
            border-radius: 2px 2px 0 0;
            min-height: 4px;
            transition: all 0.3s;

            &:hover {
              opacity: 0.8;
            }
          }
        }
      }
    }
  }
</style>
