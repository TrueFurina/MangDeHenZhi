-- ============================================================
-- 芒得很职 数据库初始化脚本
-- 仅用于首次部署，后续请使用 JPA ddl-auto: validate
-- ============================================================

CREATE DATABASE IF NOT EXISTS mangdehenzhi
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE mangdehenzhi;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    nickname VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    avatar VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    last_login_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    cover_image VARCHAR(500),
    category VARCHAR(30) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    duration INT NOT NULL,
    price DECIMAL(10,2) DEFAULT 0,
    published BOOLEAN DEFAULT FALSE,
    instructor_id BIGINT,
    enrollment_count INT DEFAULT 0,
    prerequisites TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (instructor_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测评表
CREATE TABLE IF NOT EXISTS assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20) NOT NULL,
    duration INT NOT NULL,
    total_score INT NOT NULL,
    pass_score INT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    creator_id BIGINT,
    attempt_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测评维度表
CREATE TABLE IF NOT EXISTS assessment_dimensions (
    assessment_id BIGINT NOT NULL,
    dimension VARCHAR(100) NOT NULL,
    PRIMARY KEY (assessment_id, dimension),
    FOREIGN KEY (assessment_id) REFERENCES assessments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测评结果表
CREATE TABLE IF NOT EXISTS assessment_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    score INT NOT NULL,
    passed BOOLEAN DEFAULT FALSE,
    ai_analysis TEXT,
    recommendations TEXT,
    completed_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测评维度得分表
CREATE TABLE IF NOT EXISTS assessment_dimension_scores (
    result_id BIGINT NOT NULL,
    dimension VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    PRIMARY KEY (result_id, dimension),
    FOREIGN KEY (result_id) REFERENCES assessment_results(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 证书表
CREATE TABLE IF NOT EXISTS certifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cert_hash VARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    assessment_result_id BIGINT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    blockchain_tx_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ISSUED',
    issued_at DATETIME NOT NULL,
    verified_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (assessment_result_id) REFERENCES assessment_results(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 元宇宙会话表
CREATE TABLE IF NOT EXISTS metaverse_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scene_type VARCHAR(30) NOT NULL,
    session_name VARCHAR(200),
    scene_config TEXT,
    room_id VARCHAR(100),
    active BOOLEAN DEFAULT FALSE,
    start_time DATETIME,
    end_time DATETIME,
    interaction_data TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 技能趋势表
CREATE TABLE IF NOT EXISTS skill_trends (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    demand_index DOUBLE NOT NULL,
    scarcity_index DOUBLE NOT NULL,
    growth_rate DOUBLE,
    trend VARCHAR(20),
    description TEXT,
    related_courses VARCHAR(200),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 职业路径表
CREATE TABLE IF NOT EXISTS career_paths (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    required_skills TEXT,
    recommended_courses TEXT,
    salary_range VARCHAR(50),
    demand_score DOUBLE,
    growth_potential DOUBLE,
    difficulty VARCHAR(20),
    typical_tasks TEXT,
    related_majors VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户行为事件表
CREATE TABLE IF NOT EXISTS user_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_name VARCHAR(200),
    event_data TEXT,
    page_url VARCHAR(100),
    session_id VARCHAR(50),
    ip_address VARCHAR(45),
    user_agent VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_events_user_id (user_id),
    INDEX idx_user_events_event_type (event_type),
    INDEX idx_user_events_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 职业路径表
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    company VARCHAR(200) NOT NULL,
    industry VARCHAR(100),
    location VARCHAR(100),
    description TEXT,
    requirements TEXT,
    degree VARCHAR(50),
    major VARCHAR(50),
    salary VARCHAR(100),
    apply_url VARCHAR(500),
    source VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 网申记录表
CREATE TABLE IF NOT EXISTS applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT,
    company_name VARCHAR(200),
    position_name VARCHAR(200),
    form_data TEXT,
    status VARCHAR(20) DEFAULT 'DRAFT',
    ai_suggestions TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏职位表
CREATE TABLE IF NOT EXISTS saved_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_job (user_id, job_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (job_id) REFERENCES jobs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_courses_category ON courses(category);
CREATE INDEX idx_courses_published ON courses(published);
CREATE INDEX idx_assessment_results_user ON assessment_results(user_id);
CREATE INDEX idx_certifications_user ON certifications(user_id);
CREATE INDEX idx_certifications_hash ON certifications(cert_hash);
CREATE INDEX idx_metaverse_sessions_user ON metaverse_sessions(user_id);

-- 课程课时表（支持苏格拉底提问法/费曼学习法的交互式教学单元）
CREATE TABLE IF NOT EXISTS course_lessons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    socratic_questions TEXT,
    practice_exercises TEXT,
    key_concepts TEXT,
    sort_order INT NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    estimated_minutes INT DEFAULT 15,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户学习进度表（追踪每个课时的学习状态）
CREATE TABLE IF NOT EXISTS user_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'NOT_STARTED',
    socratic_answers TEXT,
    feynman_explanation TEXT,
    practice_results TEXT,
    score INT DEFAULT 0,
    attempts INT DEFAULT 0,
    completed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_lesson (user_id, lesson_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (lesson_id) REFERENCES course_lessons(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 通知表
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    `read` BOOLEAN DEFAULT FALSE,
    related_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_notif_user (user_id),
    INDEX idx_notif_read (`read`),
    INDEX idx_notif_type (type),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;