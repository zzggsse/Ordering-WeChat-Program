<template>
  <el-card shadow="never">
    <template #header>
      <div class="row">
        <span>菜单管理</span>
        <div>
          <el-input v-model="kw" placeholder="搜索商品" style="width:200px" clearable />
          <el-button type="primary" style="margin-left:10px" @click="openAdd">新增商品</el-button>
        </div>
      </div>
    </template>

    <el-row :gutter="16">
      <el-col :span="6" v-for="p in filtered" :key="p.id">
        <el-card shadow="hover" class="item">
          <div class="imgbox">
            <img :src="imageUrl(p.image)" :alt="p.name" />
            <el-tag v-if="!p.status" class="sold" type="danger" effect="dark">售罄</el-tag>
          </div>
          <div class="name">{{ p.name }}</div>
          <div class="meta"><span class="cat">{{ p.category }}</span> <span class="price">¥{{ p.price }}</span></div>
          <div class="ops">
            <el-switch v-model="p.status" :active-value="1" :inactive-value="0" active-text="在售" inactive-text="下架" />
            <el-button size="small" text type="primary" @click="edit(p)">编辑</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '新增商品'" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名"><el-input v-model="form.name" placeholder="如：波霸奶茶" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width:100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="1" :max="99" /></el-form-item>
        <el-form-item label="商品图">
          <div class="pics">
            <div v-for="img in imageOptions" :key="img"
                 class="pic" :class="{ on: form.image === img }" @click="form.image = img">
              <img :src="imageUrl(img)" :alt="img" />
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { menuList, productImages } from '../api/mock'

const kw = ref('')
const imageUrl = (n) => productImages(n)
const list = ref(menuList)
const filtered = computed(() => list.value.filter(p => p.name.includes(kw.value)))
const categories = ['经典奶茶', '果茶', '芝士奶盖', '鲜奶', '咖啡']
const imageOptions = ['zhenzhu-naicha', 'yangzhi-manglu', 'zhizhi-naigai', 'caomei-naihui',
  'naicha-sanxongdi', 'ganlan-naivlv', 'guihua-jiuniang', 'yuyuan-xiannai', 'ningmeng-qipa', 'yaliu-naicha']

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', category: '经典奶茶', price: 12, image: imageOptions[0] })

function reset() {
  form.name = ''
  form.category = '经典奶茶'
  form.price = 12
  form.image = imageOptions[0]
}
function openAdd() { isEdit.value = false; editingId.value = null; reset(); dialogVisible.value = true }
function edit(p) {
  isEdit.value = true
  editingId.value = p.id
  form.name = p.name
  form.category = p.category
  form.price = p.price
  form.image = p.image
  dialogVisible.value = true
}
function save() {
  if (!form.name.trim()) { ElMessage.warning('请填写商品名'); return }
  if (isEdit.value) {
    const it = list.value.find(x => x.id === editingId.value)
    if (it) Object.assign(it, { name: form.name, category: form.category, price: form.price, image: form.image })
    ElMessage.success('已保存修改')
  } else {
    const maxId = list.value.reduce((m, x) => Math.max(m, x.id), 0)
    list.value.push({ id: maxId + 1, name: form.name, category: form.category, price: form.price, image: form.image, sales: 0, status: 1 })
    ElMessage.success('已新增商品')
  }
  dialogVisible.value = false
}
</script>

<style scoped>
.row{display:flex;justify-content:space-between;align-items:center}
.imgbox{position:relative;height:140px;overflow:hidden;border-radius:8px;background:#f0f0f0}
.imgbox img{width:100%;height:100%;object-fit:cover}
.sold{position:absolute;top:8px;left:8px}
.name{margin:10px 0 6px;font-weight:600}
.meta{display:flex;justify-content:space-between;color:#909399;font-size:13px}
.price{color:#e6a23c;font-weight:700}
.ops{display:flex;justify-content:space-between;align-items:center;margin-top:10px}
.pics{display:flex;flex-wrap:wrap;gap:10px}
.pic{width:64px;height:64px;border:2px solid transparent;border-radius:8px;overflow:hidden;cursor:pointer}
.pic img{width:100%;height:100%;object-fit:cover}
.pic.on{border-color:#409eff}
</style>
