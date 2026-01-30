/**
 * 认证工具类
 * 提供登录、登出、Token管理等功能
 */
const AuthUtil = {
    /**
     * 保存Token
     */
    setToken(token) {
        localStorage.setItem('token', token);
    },
    
    /**
     * 获取Token
     */
    getToken() {
        return localStorage.getItem('token');
    },
    
    /**
     * 清除Token
     */
    clearToken() {
        localStorage.removeItem('token');
        localStorage.removeItem('userInfo');
    },
    
    /**
     * 保存用户信息
     */
    setUserInfo(user) {
        localStorage.setItem('userInfo', JSON.stringify(user));
    },
    
    /**
     * 获取用户信息
     */
    getUserInfo() {
        const userInfo = localStorage.getItem('userInfo');
        return userInfo ? JSON.parse(userInfo) : null;
    },
    
    /**
     * 检查是否已登录
     */
    isLoggedIn() {
        return !!this.getToken();
    },
    
    /**
     * 跳转到登录页
     */
    redirectToLogin() {
        window.location.href = '/html/login.html';
    },
    
    /**
     * 获取请求头（包含Token）
     */
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        const token = this.getToken();
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }
        return headers;
    },
    
    /**
     * 检查登录状态，如果未登录则跳转
     */
    requireAuth() {
        if (!this.isLoggedIn()) {
            alert('请先登录');
            this.redirectToLogin();
            return false;
        }
        return true;
    }
};

/**
 * API工具类
 * 提供统一的API请求方法，自动处理Token和错误
 */
const ApiUtil = {
    /**
     * 通用请求方法
     * @param {string} url 请求URL
     * @param {object} options 请求选项
     * @returns {Promise} 响应数据
     */
    async request(url, options = {}) {
        const headers = AuthUtil.getHeaders();
        
        const config = {
            ...options,
            headers: {
                ...headers,
                ...(options.headers || {})
            }
        };
        
        try {
            const response = await fetch(url, config);
            
            // 如果返回401，说明Token过期或无效，跳转到登录页
            if (response.status === 401) {
                AuthUtil.clearToken();
                alert('登录已过期，请重新登录');
                AuthUtil.redirectToLogin();
                return null;
            }
            
            const result = await response.json();
            
            // 如果返回错误，抛出异常
            if (!result.status && result.code !== 200) {
                throw new Error(result.message || '请求失败');
            }
            
            return result;
        } catch (error) {
            console.error('API请求错误:', error);
            throw error;
        }
    },
    
    /**
     * GET请求
     */
    async get(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return this.request(fullUrl, {
            method: 'GET'
        });
    },
    
    /**
     * POST请求
     */
    async post(url, data = {}) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    /**
     * PUT请求
     */
    async put(url, data = {}) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    /**
     * DELETE请求
     */
    async delete(url) {
        return this.request(url, {
            method: 'DELETE'
        });
    },
    
    /**
     * 登录
     */
    async login(username, password) {
        const result = await this.post('/api/auth/login', {
            username: username,
            password: password
        });
        
        if (result && result.data) {
            AuthUtil.setToken(result.data.token);
            AuthUtil.setUserInfo(result.data.user);
            return result.data;
        }
        
        throw new Error(result?.message || '登录失败');
    },
    
    /**
     * 登出
     */
    async logout() {
        try {
            await this.post('/api/auth/logout');
        } catch (error) {
            console.error('登出错误:', error);
        } finally {
            AuthUtil.clearToken();
        }
    },
    
    /**
     * 刷新Token
     */
    async refreshToken() {
        const result = await this.post('/api/auth/refresh');
        if (result && result.data) {
            AuthUtil.setToken(result.data.token);
            AuthUtil.setUserInfo(result.data.user);
            return result.data;
        }
        throw new Error('刷新Token失败');
    }
};

