<template>
  <div ref="containerRef" class="three-scene" />
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { CSS2DRenderer, CSS2DObject } from 'three/addons/renderers/CSS2DRenderer.js'

const props = defineProps<{
  sceneType: 'INTERVIEW_ROOM' | 'CLASSROOM' | 'MEETING_ROOM' | 'TRAINING_ROOM'
  aiCharacterCount?: number
}>()

const emit = defineEmits<{
  sceneReady: []
}>()

const containerRef = ref<HTMLDivElement>()

let scene: THREE.Scene
let camera: THREE.PerspectiveCamera
let renderer: THREE.WebGLRenderer
let labelRenderer: CSS2DRenderer
let controls: OrbitControls
let animationId: number
let lights: THREE.Light[] = []

const SCENE_CONFIGS: Record<string, { bgColor: number; groundColor: number; lightIntensity: number }> = {
  INTERVIEW_ROOM: { bgColor: 0x1a1b2e, groundColor: 0x2d2d4a, lightIntensity: 0.8 },
  CLASSROOM: { bgColor: 0x1a2a1a, groundColor: 0x2d4a2d, lightIntensity: 1.0 },
  MEETING_ROOM: { bgColor: 0x2a1a2e, groundColor: 0x4a2d4a, lightIntensity: 0.9 },
  TRAINING_ROOM: { bgColor: 0x1a2a2e, groundColor: 0x2d4a4a, lightIntensity: 1.2 },
}

function initScene() {
  if (!containerRef.value) return

  const container = containerRef.value
  const width = container.clientWidth
  const height = container.clientHeight
  const config = SCENE_CONFIGS[props.sceneType] || SCENE_CONFIGS.INTERVIEW_ROOM

  // === 场景 ===
  scene = new THREE.Scene()
  scene.background = new THREE.Color(config.bgColor)
  scene.fog = new THREE.Fog(config.bgColor, 20, 50)

  // === 相机 ===
  camera = new THREE.PerspectiveCamera(45, width / height, 0.1, 100)
  camera.position.set(8, 6, 12)
  camera.lookAt(0, 0, 0)

  // === WebGL 渲染器 ===
  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(width, height)
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
  renderer.shadowMap.enabled = true
  renderer.shadowMap.type = THREE.PCFSoftShadowMap
  renderer.toneMapping = THREE.ACESFilmicToneMapping
  renderer.toneMappingExposure = 1.2
  container.appendChild(renderer.domElement)

  // === CSS2D 标签渲染器 ===
  labelRenderer = new CSS2DRenderer()
  labelRenderer.setSize(width, height)
  labelRenderer.domElement.style.position = 'absolute'
  labelRenderer.domElement.style.top = '0'
  labelRenderer.domElement.style.pointerEvents = 'none'
  container.appendChild(labelRenderer.domElement)

  // === 控制器 ===
  controls = new OrbitControls(camera, renderer.domElement)
  controls.enableDamping = true
  controls.dampingFactor = 0.08
  controls.maxPolarAngle = Math.PI / 2.2
  controls.minDistance = 3
  controls.maxDistance = 25
  controls.target.set(0, 1.5, 0)

  // === 灯光 ===
  const ambient = new THREE.AmbientLight(0x404060, 0.5)
  scene.add(ambient)

  const mainLight = new THREE.DirectionalLight(0xffffff, config.lightIntensity)
  mainLight.position.set(10, 15, 10)
  mainLight.castShadow = true
  mainLight.shadow.mapSize.width = 2048
  mainLight.shadow.mapSize.height = 2048
  scene.add(mainLight)

  const fillLight = new THREE.DirectionalLight(0x8888ff, 0.3)
  fillLight.position.set(-5, 5, -10)
  scene.add(fillLight)

  const rimLight = new THREE.DirectionalLight(0xffffff, 0.2)
  rimLight.position.set(0, -5, 10)
  scene.add(rimLight)

  lights = [ambient, mainLight, fillLight, rimLight]

  // === 地面 ===
  createGround(config.groundColor)

  // === 场景特定元素 ===
  createSceneElements()

  // === 动画循环 ===
  animate()

  emit('sceneReady')
}

function createGround(color: number) {
  const groundGeo = new THREE.PlaneGeometry(30, 30)
  const groundMat = new THREE.MeshStandardMaterial({
    color,
    roughness: 0.8,
    metalness: 0.1,
    transparent: true,
    opacity: 0.9,
  })
  const ground = new THREE.Mesh(groundGeo, groundMat)
  ground.rotation.x = -Math.PI / 2
  ground.position.y = -0.01
  ground.receiveShadow = true
  scene.add(ground)

  // 网格辅助线
  const gridHelper = new THREE.GridHelper(30, 30, 0x6666aa, 0x444466)
  gridHelper.position.y = 0
  scene.add(gridHelper)
}

function createSceneElements() {
  switch (props.sceneType) {
    case 'INTERVIEW_ROOM':
      createInterviewRoom()
      break
    case 'CLASSROOM':
      createClassroom()
      break
    case 'MEETING_ROOM':
      createMeetingRoom()
      break
    case 'TRAINING_ROOM':
      createTrainingRoom()
      break
  }
}

function createDesk(w: number, h: number, d: number, color: number, x: number, z: number) {
  const geo = new THREE.BoxGeometry(w, h, d)
  const mat = new THREE.MeshStandardMaterial({ color, roughness: 0.5, metalness: 0.3 })
  const desk = new THREE.Mesh(geo, mat)
  desk.position.set(x, h / 2, z)
  desk.castShadow = true
  desk.receiveShadow = true
  scene.add(desk)
  return desk
}

function createChair(x: number, z: number, color = 0x444466) {
  const group = new THREE.Group()
  // 座垫
  const seat = new THREE.Mesh(
    new THREE.BoxGeometry(0.8, 0.15, 0.8),
    new THREE.MeshStandardMaterial({ color, roughness: 0.7 })
  )
  seat.position.y = 0.4
  seat.castShadow = true
  group.add(seat)

  // 靠背
  const back = new THREE.Mesh(
    new THREE.BoxGeometry(0.8, 0.6, 0.1),
    new THREE.MeshStandardMaterial({ color, roughness: 0.7 })
  )
  back.position.set(0, 0.75, -0.4)
  group.add(back)

  // 椅子腿
  const legMat = new THREE.MeshStandardMaterial({ color: 0x888888, metalness: 0.8, roughness: 0.2 })
  for (const [lx, lz] of [[-0.35, -0.35], [0.35, -0.35], [-0.35, 0.35], [0.35, 0.35]]) {
    const leg = new THREE.Mesh(new THREE.CylinderGeometry(0.04, 0.04, 0.35), legMat)
    leg.position.set(lx, 0.175, lz)
    group.add(leg)
  }

  group.position.set(x, 0, z)
  group.rotation.y = Math.PI
  scene.add(group)
  return group
}

function createAICharacter(name: string, x: number, z: number, color = 0x4488ff) {
  const group = new THREE.Group()

  // 身体
  const bodyMat = new THREE.MeshStandardMaterial({ color, roughness: 0.6, metalness: 0.1 })
  const body = new THREE.Mesh(new THREE.CylinderGeometry(0.4, 0.5, 1.2), bodyMat)
  body.position.y = 0.6
  body.castShadow = true
  group.add(body)

  // 头
  const head = new THREE.Mesh(
    new THREE.SphereGeometry(0.25, 16, 16),
    new THREE.MeshStandardMaterial({ color: 0xffccaa, roughness: 0.5 })
  )
  head.position.y = 1.4
  head.castShadow = true
  group.add(head)

  // 发光光环（AI标识）
  const glowRing = new THREE.Mesh(
    new THREE.TorusGeometry(0.35, 0.03, 8, 24),
    new THREE.MeshBasicMaterial({ color: 0x00ccff, transparent: true, opacity: 0.6 })
  )
  glowRing.position.y = 1.6
  glowRing.rotation.x = Math.PI / 2
  group.add(glowRing)

  // 标签
  const labelDiv = document.createElement('div')
  labelDiv.textContent = `🤖 ${name}`
  labelDiv.style.color = '#00ccff'
  labelDiv.style.fontSize = '14px'
  labelDiv.style.fontWeight = 'bold'
  labelDiv.style.textShadow = '0 0 10px rgba(0,204,255,0.5)'
  labelDiv.style.background = 'rgba(0,0,0,0.6)'
  labelDiv.style.padding = '4px 12px'
  labelDiv.style.borderRadius = '12px'
  labelDiv.style.border = '1px solid rgba(0,204,255,0.3)'
  const label = new CSS2DObject(labelDiv)
  label.position.set(0, 2.0, 0)
  group.add(label)

  group.position.set(x, 0, z)
  scene.add(group)
  return group
}

function createInterviewRoom() {
  // 面试桌
  createDesk(2.5, 0.1, 1.2, 0x334466, 0, 0)

  // 面试官座位
  createChair(0, 1.5)
  // 应聘者座位
  createChair(0, -1.5, 0x664444)

  // AI 角色
  createAICharacter('面试官', 0, 1.5, 0x4488ff)
  if ((props.aiCharacterCount ?? 1) >= 2) {
    createAICharacter('HR 专员', 1.8, 1.0, 0x88aaff)
  }
  if ((props.aiCharacterCount ?? 1) >= 3) {
    createAICharacter('技术主管', -1.8, 1.0, 0x6688dd)
  }
}

function createClassroom() {
  // 讲台
  createDesk(1.5, 0.15, 0.8, 0x446644, 0, 3.5)

  // 学生桌椅（3排）
  for (let row = 0; row < 3; row++) {
    for (let col = -1; col <= 1; col++) {
      createDesk(1.0, 0.08, 0.6, 0x335533, col * 1.8, -row * 1.8 + 1)
      createChair(col * 1.8, -row * 1.8 + 1.8, 0x446644)
    }
  }

  // 讲师
  createAICharacter('讲师', 0, 3.5, 0x44aa44)
}

function createMeetingRoom() {
  // 会议桌
  createDesk(3.0, 0.1, 1.8, 0x444466, 0, 0)

  // 座位围绕会议桌
  const positions = [
    [0, 2.0], [1.8, 1.2], [1.8, -1.2], [0, -2.0], [-1.8, -1.2], [-1.8, 1.2]
  ]
  positions.forEach(([x, z], i) => {
    createChair(x, z, 0x554466)
    if (i < (props.aiCharacterCount ?? 2)) {
      createAICharacter(`参会者${i + 1}`, x, z, 0x8888cc)
    }
  })
}

function createTrainingRoom() {
  // 训练区域
  createDesk(2.0, 0.1, 1.0, 0x446666, 0, 0)

  // 训练设备（模拟立方体）
  const deviceMat = new THREE.MeshStandardMaterial({
    color: 0x44aaaa,
    roughness: 0.3,
    metalness: 0.7,
    emissive: 0x226666,
    emissiveIntensity: 0.1,
  })
  for (let i = -1; i <= 1; i++) {
    const device = new THREE.Mesh(new THREE.BoxGeometry(0.3, 0.6, 0.3), deviceMat)
    device.position.set(i * 1.2, 0.3, 1.5)
    device.castShadow = true
    scene.add(device)
  }

  // 培训师
  createAICharacter('培训师', 0, 2.5, 0x44aaaa)
  if ((props.aiCharacterCount ?? 1) >= 2) {
    createAICharacter('助教', 1.8, 2.0, 0x66cccc)
  }
}

function animate() {
  animationId = requestAnimationFrame(animate)
  controls.update()
  renderer.render(scene, camera)
  labelRenderer.render(scene, camera)
}

function onResize() {
  if (!containerRef.value) return
  const width = containerRef.value.clientWidth
  const height = containerRef.value.clientHeight
  camera.aspect = width / height
  camera.updateProjectionMatrix()
  renderer.setSize(width, height)
  labelRenderer.setSize(width, height)
}

onMounted(() => {
  initScene()
  window.addEventListener('resize', onResize)
})

onBeforeUnmount(() => {
  cancelAnimationFrame(animationId)
  window.removeEventListener('resize', onResize)
  renderer?.dispose()
  labelRenderer?.domElement.remove()
  // Cleanup scene objects
  scene?.traverse((obj) => {
    if (obj instanceof THREE.Mesh) {
      obj.geometry.dispose()
      if (Array.isArray(obj.material)) {
        obj.material.forEach(m => m.dispose())
      } else {
        obj.material.dispose()
      }
    }
  })
})
</script>

<style scoped>
.three-scene {
  width: 100%;
  height: 100%;
  min-height: 500px;
  position: relative;
  overflow: hidden;
  border-radius: 12px;
}
</style>