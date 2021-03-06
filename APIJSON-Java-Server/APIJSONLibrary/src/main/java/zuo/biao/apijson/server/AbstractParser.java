/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson.server;

import static zuo.biao.apijson.RequestMethod.GET;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.ConditionErrorException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.exception.NotLoggedInException;
import zuo.biao.apijson.server.exception.OutOfRangeException;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public abstract class AbstractParser implements Parser {
	protected static final String TAG = "AbstractParser";


	/**
	 * GET
	 */
	public AbstractParser() {
		this(null);
	}
	/**
	 * @param requestMethod null ? requestMethod = GET
	 */
	public AbstractParser(RequestMethod method) {
		this(method, false);
	}


	/**
	 * @param requestMethod null ? requestMethod = GET
	 * @param noVerify 仅限于为服务端提供方法免验证特权，普通请求不要设置为true！ 如果对应Table有权限也建议用默认值false，保持和客户端权限一致
	 */
	public AbstractParser(RequestMethod method, boolean noVerify) {
		super();
		setMethod(method);
		setNoVerify(noVerify);
	}

	@NotNull
	protected Visitor visitor;
	@NotNull
	@Override
	public Visitor getVisitor() {
		if (visitor == null) {
			visitor = new Visitor() {
				
				@Override
				public Long getId() {
					return 0L;
				}
				
				@Override
				public List<Long> getContactIdList() {
					return null;
				}
			};
		}
		return visitor;
	}
	@Override
	public AbstractParser setVisitor(@NotNull Visitor visitor) {
		this.visitor = visitor;
		return this;
	}

	protected RequestMethod requestMethod;
	@NotNull
	@Override
	public RequestMethod getMethod() {
		return requestMethod;
	}
	@NotNull
	@Override
	public AbstractParser setMethod(RequestMethod method) {
		this.requestMethod = method == null ? GET : method;
		return this;
	}

	protected JSONObject requestObject;
	@Override
	public JSONObject getRequest() {
		return requestObject;
	}
	@Override
	public AbstractParser setRequest(JSONObject request) {
		this.requestObject = request;
		return this;
	}


	protected Verifier verifier;
	protected RequestRole globleRole;
	public AbstractParser setGlobleRole(RequestRole globleRole) {
		this.globleRole = globleRole;
		return this;
	}

	@Override
	public boolean isNoVerify() {
		return noVerifyLogin && noVerifyRole && noVerifyContent;
	}
	@Override
	public AbstractParser setNoVerify(boolean noVerify) {
		setNoVerifyLogin(noVerify);
		setNoVerifyRole(noVerify);
		setNoVerifyContent(noVerify);
		return this;
	}

	protected boolean noVerifyLogin;
	@Override
	public boolean isNoVerifyLogin() {
		return noVerifyLogin;
	}
	@Override
	public AbstractParser setNoVerifyLogin(boolean noVerifyLogin) {
		this.noVerifyLogin = noVerifyLogin;
		return this;
	}
	protected boolean noVerifyRole;
	@Override
	public boolean isNoVerifyRole() {
		return noVerifyRole;
	}
	@Override
	public AbstractParser setNoVerifyRole(boolean noVerifyRole) {
		this.noVerifyRole = noVerifyRole;
		return this;
	}
	protected boolean noVerifyContent;
	@Override
	public boolean isNoVerifyContent() {
		return noVerifyContent;
	}
	@Override
	public AbstractParser setNoVerifyContent(boolean noVerifyContent) {
		this.noVerifyContent = noVerifyContent;
		return this;
	}





	protected SQLExecutor sqlExecutor;
	protected Map<String, Object> queryResultMap;//path-result


	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	@Override
	public String parse(String request) {
		return JSON.toJSONString(parseResponse(request));
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	@NotNull
	@Override
	public String parse(JSONObject request) {
		return JSON.toJSONString(parseResponse(request));
	}

	/**解析请求json并获取对应结果
	 * @param request 先parseRequest中URLDecoder.decode(request, UTF_8);再parseResponse(getCorrectRequest(...))
	 * @return parseResponse(requestObject);
	 */
	@NotNull
	@Override
	public JSONObject parseResponse(String request) {
		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"
				+ requestMethod + "/parseResponse  request = \n" + request + "\n\n");

		try {
			requestObject = parseRequest(request);
		} catch (Exception e) {
			return newErrorResult(e);
		}

		return parseResponse(requestObject);
	}

	/**解析请求json并获取对应结果
	 * @param request
	 * @return requestObject
	 */
	@NotNull
	@Override
	public JSONObject parseResponse(JSONObject request) {
		long startTime = System.currentTimeMillis();
		Log.d(TAG, "parseResponse  startTime = " + startTime
				+ "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n\n\n ");

		requestObject = request;

		verifier = createVerifier().setVisitor(getVisitor());

		if (RequestMethod.isPublicMethod(requestMethod) == false) {
			try {
				if (noVerifyLogin == false) {
					onVerifyLogin();
				}
				if (noVerifyContent == false) {
					onVerifyContent();
				}
			} catch (Exception e) {
				return extendErrorResult(requestObject, e);
			}
		}

		//必须在parseCorrectRequest后面，因为parseCorrectRequest可能会添加 @role
		if (noVerifyRole == false && globleRole == null) {
			try {
				setGlobleRole(RequestRole.get(requestObject.getString(JSONRequest.KEY_ROLE)));
				requestObject.remove(JSONRequest.KEY_ROLE);
			} catch (Exception e) {
				return extendErrorResult(requestObject, e);
			}
		}

		final String requestString = JSON.toJSONString(request);//request传进去解析后已经变了


		queryResultMap = new HashMap<String, Object>();

		Exception error = null;
		sqlExecutor = createSQLExecutor();
		try {
			requestObject = onObjectParse(request, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}
		sqlExecutor.close();
		sqlExecutor = null;


		requestObject = verifier.removeAccessInfo(requestObject);
		requestObject = error == null ? extendSuccessResult(requestObject) : extendErrorResult(requestObject, error);


		queryResultMap.clear();

		//会不会导致原来的session = null？		session = null;


		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n "
				+ requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");

		Log.d(TAG, "parse  return response = \n" + JSON.toJSONString(requestObject)
		+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n\n");

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "parseResponse  endTime = " + endTime + ";  duration = " + (endTime - startTime)
				+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");

		return requestObject;
	}


	protected void onVerifyLogin() throws Exception {
		verifier.verifyLogin();
	}
	protected void onVerifyContent() throws Exception {
		requestObject = parseCorrectRequest();
	}
	
	
	/**解析请求JSONObject
	 * @param request => URLDecoder.decode(request, UTF_8);
	 * @return
	 * @throws Exception 
	 */
	@NotNull
	public static JSONObject parseRequest(String request) throws Exception {
		JSONObject obj = JSON.parseObject(request);
		if (obj == null) {
			throw new UnsupportedEncodingException("JSON格式不合法！");
		}
		return obj;
	}





	/**新建带状态内容的JSONObject
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject newResult(int code, String msg) {
		return extendResult(null, code, msg);
	}
	/**添加JSONObject的状态内容，一般用于错误提示结果
	 * @param object
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject extendResult(JSONObject object, int code, String msg) {
		if (object == null) {
			object = new JSONObject(true);
		}
		if (object.containsKey(JSONResponse.KEY_CODE) == false) {
			object.put(JSONResponse.KEY_CODE, code);
		}
		String m = StringUtil.getString(object.getString(JSONResponse.KEY_MSG));
		if (m.isEmpty() == false) {
			msg = m + " ;\n " + StringUtil.getString(msg);
		}
		object.put(JSONResponse.KEY_MSG, msg);
		return object;
	}


	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object) {
		return extendResult(object, JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED);
	}
	/**获取请求成功的状态内容
	 * @return
	 */
	public static JSONObject newSuccessResult() {
		return newResult(JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED);
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Exception e) {
		JSONObject error = newErrorResult(e);
		return extendResult(object, error.getIntValue(JSONResponse.KEY_CODE), error.getString(JSONResponse.KEY_MSG));
	}
	/**新建错误状态内容
	 * @param e
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e) {
		if (e != null) {
			e.printStackTrace();

			int code;
			if (e instanceof UnsupportedEncodingException) {
				code = JSONResponse.CODE_UNSUPPORTED_ENCODING;
			} 
			else if (e instanceof IllegalAccessException) {
				code = JSONResponse.CODE_ILLEGAL_ACCESS;
			}
			else if (e instanceof UnsupportedOperationException) {
				code = JSONResponse.CODE_UNSUPPORTED_OPERATION;
			}
			else if (e instanceof NotExistException) {
				code = JSONResponse.CODE_NOT_FOUND;
			}
			else if (e instanceof IllegalArgumentException) {
				code = JSONResponse.CODE_ILLEGAL_ARGUMENT;
			}
			else if (e instanceof NotLoggedInException) {
				code = JSONResponse.CODE_NOT_LOGGED_IN;
			}
			else if (e instanceof TimeoutException) {
				code = JSONResponse.CODE_TIME_OUT;
			} 
			else if (e instanceof ConflictException) {
				code = JSONResponse.CODE_CONFLICT;
			}
			else if (e instanceof ConditionErrorException) {
				code = JSONResponse.CODE_CONDITION_ERROR;
			}
			else if (e instanceof UnsupportedDataTypeException) {
				code = JSONResponse.CODE_UNSUPPORTED_TYPE;
			}
			else if (e instanceof OutOfRangeException) {
				code = JSONResponse.CODE_OUT_OF_RANGE;
			}
			else if (e instanceof NullPointerException) {
				code = JSONResponse.CODE_NULL_POINTER;
			}
			else {
				code = JSONResponse.CODE_SERVER_ERROR;
			}

			return newResult(code, e.getMessage());
		}

		return newResult(JSONResponse.CODE_SERVER_ERROR, JSONResponse.MSG_SERVER_ERROR);
	}




	//TODO 启动时一次性加载Request所有内容，作为初始化。
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject parseCorrectRequest() throws Exception {
		if (RequestMethod.isPublicMethod(requestMethod)) {
			return requestObject;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = requestObject.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请设置tag！一般是Table名");
		}
		int version = requestObject.getIntValue(JSONRequest.KEY_VERSION);

		JSONObject object = null;
		String error = "";
		try {
			object = getStructure("Request", JSONRequest.KEY_TAG, tag, version);
		} catch (Exception e) {
			error = e.getMessage();
		}
		if (object == null) {//empty表示随意操作  || object.isEmpty()) {
			throw new UnsupportedOperationException("非开放请求必须是Request表中校验规则允许的操作！\n " + error);
		}

		JSONObject target = null;
		if (zuo.biao.apijson.JSONObject.isTableKey(tag) && object.containsKey(tag) == false) {//tag是table名
			target = new JSONObject(true);
			target.put(tag, object);
		} else {
			target = object;
		}
		//获取指定的JSON结构 >>>>>>>>>>>>>>

		requestObject.remove(JSONRequest.KEY_TAG);
		requestObject.remove(JSONRequest.KEY_VERSION);
		return parseCorrectRequest((JSONObject) target.clone());
	}


	//TODO 优化性能！
	/**获取正确的返回结果
	 * @param method
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject parseCorrectResponse(String table, JSONObject response) throws Exception {
		//		Log.d(TAG, "getCorrectResponse  method = " + method + "; table = " + table);
		//		if (response == null || response.isEmpty()) {//避免无效空result:{}添加内容后变有效
		//			Log.e(TAG, "getCorrectResponse  response == null || response.isEmpty() >> return response;");
		return response;
		//		}
		//
		//		JSONObject target = zuo.biao.apijson.JSONObject.isTableKey(table) == false
		//				? new JSONObject() : getStructure(method, "Response", "model", table);
		//
		//				return MethodStructure.parseResponse(method, table, target, response, new OnParseCallback() {
		//
		//					@Override
		//					protected JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
		//						return getCorrectResponse(method, key, robj);
		//					}
		//				});
	}

	/**获取Request或Response内指定JSON结构
	 * @param table
	 * @param key
	 * @param value
	 * @param version
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getStructure(@NotNull String table, String key, String value, int version) throws Exception  {
		//获取指定的JSON结构 <<<<<<<<<<<<<<
		SQLConfig config = createSQLConfig().setMethod(GET).setTable(table);
		config.setPrepared(false);
		config.setColumn("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", requestMethod.name());
		if (key != null) {
			where.put(key, value);
		}
		if (version > 0) {
			where.put(JSONRequest.KEY_VERSION + "{}", ">=" + version);
		}
		config.setWhere(where);
		config.setOrder(JSONRequest.KEY_VERSION + (version > 0 ? "+" : "-"));
		config.setCount(1);

		SQLExecutor executor = createSQLExecutor();

		//too many connections error: 不try-catch，可以让客户端看到是服务器内部异常
		try {
			JSONObject result = executor.execute(config.setCacheStatic(true));
			return getJSONObject(result, "structure");//解决返回值套了一层 "structure":{}
		} finally {
			executor.close();
		}
	}



	//	protected SQLConfig itemConfig;
	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @param config for array item
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject onObjectParse(final JSONObject request
			, String parentPath, String name, final SQLConfig arrayConfig) throws Exception {
		Log.i(TAG, "\ngetObject:  parentPath = " + parentPath
				+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {// Moment:{}   || request.isEmpty()) {//key-value条件
			return null;
		}
		
		int type = arrayConfig == null ? 0 : arrayConfig.getType();

		ObjectParser op = createObjectParser(request, parentPath, name, arrayConfig).parse();


		JSONObject response = null;
		if (op != null) {//TODO SQL查询结果为空时，functionMap和customMap还有没有意义？
			if (arrayConfig == null) {//Common
				response = op.executeSQL().response();
			} else {//Array Item Child
				int query = arrayConfig.getQuery();

				//total 这里不能用arrayConfig.getType()，因为在createObjectParser.onChildParse传到onObjectParse时已被改掉
				if (type == SQLConfig.TYPE_ITEM_CHILD_0 && query != JSONRequest.QUERY_TABLE
						&& arrayConfig.getPosition() == 0) {
					JSONObject rp = op.setMethod(RequestMethod.HEAD).executeSQL().getSqlReponse();
					if (rp != null) {
						int index = parentPath.lastIndexOf("]/");
						if (index >= 0) {
							int total = rp.getIntValue(JSONResponse.KEY_COUNT);
							putQueryResult(parentPath.substring(0, index) + "]/" + JSONResponse.KEY_TOTAL, total);

							if (total <= arrayConfig.getCount()*arrayConfig.getPage()) {
								query = JSONRequest.QUERY_TOTAL;//数量不够了，不再往后查询
							}
						}
					}

					op.setMethod(requestMethod);
				}

				//Table
				if (query == JSONRequest.QUERY_TOTAL) {
					response = null;//不再往后查询
				} else {
					response = op.executeSQL(
							arrayConfig.getCount(), arrayConfig.getPage(), arrayConfig.getPosition()
							).response();
					//					itemConfig = op.getConfig();
				}
			}

			op.recycle();
			op = null;
		}

		return response;
	}

	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return 
	 * @throws Exception
	 */
	@Override
	public JSONArray onArrayParse(final JSONObject request, String parentPath, String name) throws Exception {
		Log.i(TAG, "\n\n\n getArray parentPath = " + parentPath
				+ "; name = " + name + "; request = " + JSON.toJSONString(request));
		//不能允许GETS，否则会被通过"[]":{"@role":"ADMIN"},"Table":{},"tag":"Table"绕过权限并能批量查询
		if (RequestMethod.isGetMethod(requestMethod, false) == false) {
			throw new UnsupportedOperationException("key[]:{}只支持GET方法！不允许传 " + name + ":{} ！");
		}
		if (request == null || request.isEmpty()) {//jsonKey-jsonValue条件
			return null;
		}
		String path = getAbsPath(parentPath, name);

		//不能改变，因为后面可能继续用到，导致1以上都改变 []:{0:{Comment[]:{0:{Comment:{}},1:{...},...}},1:{...},...}
		final int query = request.getIntValue(JSONRequest.KEY_QUERY);
		final int count = request.getIntValue(JSONRequest.KEY_COUNT);
		final int page = request.getIntValue(JSONRequest.KEY_PAGE);
		request.remove(JSONRequest.KEY_QUERY);
		request.remove(JSONRequest.KEY_COUNT);
		request.remove(JSONRequest.KEY_PAGE);
		Log.d(TAG, "getArray  query = " + query + "; count = " + count + "; page = " + page);

		if (request.isEmpty()) {//如果条件成立，说明所有的 parentPath/name:request 中request都无效！！！
			Log.e(TAG, "getArray  request.isEmpty() >> return null;");
			return null;
		}


		//不用total限制数量了，只用中断机制，total只在query = 1,2的时候才获取
		int size = count <= 0 || count > 100 ? 100 : count;//count为每页数量，size为第page页实际数量，max(size) = count
		Log.d(TAG, "getArray  size = " + size + "; page = " + page);


		//key[]:{Table:{}}中key equals Table时 提取Table
		int index = name == null ? -1 : name.lastIndexOf("[]");
		String childPath = index <= 0 ? null : Pair.parseEntry(name.substring(0, index), true).getKey(); // Table-key1-key2...

		//判断第一个key，即Table是否存在，如果存在就提取
		String[] childKeys = StringUtil.split(childPath, "-", false);
		if (childKeys == null || childKeys.length <= 0 || request.containsKey(childKeys[0]) == false) {
			childKeys = null;
		}


		//Table<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONArray response = new JSONArray();
		SQLConfig config = createSQLConfig()
				.setMethod(requestMethod)
				.setCount(size)
				.setPage(page)
				.setQuery(query);

		JSONObject parent;
		//生成size个
		for (int i = 0; i < size; i++) {
			parent = onObjectParse(request, path, "" + i, config.setType(SQLConfig.TYPE_ITEM).setPosition(i));
			if (parent == null || parent.isEmpty()) {
				break;
			}
			//key[]:{Table:{}}中key equals Table时 提取Table
			response.add(getValue(parent, childKeys)); //null有意义
		}
		//Table>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		
		/*
		 * 支持引用取值后的数组
			{
			    "User-id[]": {
			        "User": {
			            "contactIdList<>": 82002
			        }
			    },
			    "Moment-userId[]": {
			        "Moment": {
			            "userId{}@": "User-id[]"
			        }
			    }
			}
		 */
		Object fo = response.isEmpty() ? null : response.get(0); //可能是远程函数返回的,而不是提取 childKeys == null || response.isEmpty()
		if (fo instanceof Boolean || fo instanceof Number || fo instanceof String) { //[{}] 和 [[]] 都没意义
			putQueryResult(path, response);
		}
		
		
		//后面还可能用到，要还原
		request.put(JSONRequest.KEY_QUERY, query);
		request.put(JSONRequest.KEY_COUNT, count);
		request.put(JSONRequest.KEY_PAGE, page);

		Log.i(TAG, "getArray  return response = \n" + JSON.toJSONString(response) + "\n>>>>>>>>>>>>>>>\n\n\n");
		return response;
	}


	/**根据路径取值
	 * @param parent
	 * @param pathKeys
	 * @return
	 */
	protected static Object getValue(JSONObject parent, String[] pathKeys) {
		if (parent == null || pathKeys == null || pathKeys.length <= 0) {
			Log.w(TAG, "getChild  parent == null || pathKeys == null || pathKeys.length <= 0 >> return parent;");
			return parent;
		}

		//逐层到达child的直接容器JSONObject parent
		final int last = pathKeys.length - 1;
		for (int i = 0; i < last; i++) {//一步一步到达指定位置
			if (parent == null) {//不存在或路径错误(中间的key对应value不是JSONObject)
				break;
			}
			parent = getJSONObject(parent, pathKeys[i]);
		}

		return parent == null ? null : parent.get(pathKeys[last]);
	}


	/**获取被依赖引用的key的路径, 实时替换[] -> []/i
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String getValuePath(String parentPath, String valuePath) {
		if (valuePath.startsWith("/")) {
			valuePath = getAbsPath(parentPath, valuePath);
		} else {//处理[] -> []/i
			valuePath = replaceArrayChildPath(parentPath, valuePath);
		}
		return valuePath;
	}

	/**获取绝对路径
	 * @param path
	 * @param name
	 * @return
	 */
	public static String getAbsPath(String path, String name) {
		Log.i(TAG, "getPath  path = " + path + "; name = " + name + " <<<<<<<<<<<<<");
		path = StringUtil.getString(path);
		name = StringUtil.getString(name);
		if (StringUtil.isNotEmpty(path, false)) {
			if (StringUtil.isNotEmpty(name, false)) {
				path += ((name.startsWith("/") ? "" : "/") + name);
			}
		} else {
			path = name;
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		Log.i(TAG, "getPath  return " + path + " >>>>>>>>>>>>>>>>");
		return path;
	}

	/**替换[] -> []/i
	 * 不能写在getAbsPath里，因为name不一定是依赖路径
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String replaceArrayChildPath(String parentPath, String valuePath) {
		String[] ps = StringUtil.split(parentPath, "]/");//"[]/");
		if (ps != null && ps.length > 1) {
			String[] vs = StringUtil.split(valuePath, "]/");

			if (vs != null && vs.length > 0) {
				String pos;
				for (int i = 0; i < ps.length - 1; i++) {
					if (ps[i] == null || ps[i].equals(vs[i]) == false) {//允许""？
						break;
					}

					pos = ps[i+1].contains("/") == false ? ps[i+1]
							: ps[i+1].substring(0, ps[i+1].indexOf("/"));
					if (
							//StringUtil.isNumer(pos) && 
							vs[i+1].startsWith(pos + "/") == false) {
						vs[i+1] = pos + "/" + vs[i+1];
					}
				}
				return StringUtil.getString(vs, "]/");
			}
		}
		return valuePath;
	}

	//依赖引用关系 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**将已获取完成的object的内容替换requestObject里对应的值
	 * @param path object的路径
	 * @param result 需要被关联的object
	 */
	@Override
	public synchronized void putQueryResult(String path, Object result) {
		Log.i(TAG, "\n putQueryResult  valuePath = " + path + "; result = " + result + "\n <<<<<<<<<<<<<<<<<<<<<<<");
		//		if (queryResultMap.containsKey(valuePath)) {//只保存被关联的value
		Log.d(TAG, "putQueryResult  queryResultMap.containsKey(valuePath) >> queryResultMap.put(path, result);");
		queryResultMap.put(path, result);
		//		}
	}
	/**根据路径获取值
	 * @param valuePath
	 * @return parent == null ? valuePath : parent.get(keys[keys.length - 1])
	 */
	@Override
	public Object getValueByPath(String valuePath) {
		Log.i(TAG, "<<<<<<<<<<<<<<< \n getValueByPath  valuePath = " + valuePath + "\n <<<<<<<<<<<<<<<<<<");
		if (StringUtil.isEmpty(valuePath, true)) {
			Log.e(TAG, "getValueByPath  StringUtil.isNotEmpty(valuePath, true) == false >> return null;");
			return null;
		}
		Object target = queryResultMap.get(valuePath);
		if (target != null) {
			return target;
		}

		//取出key被valuePath包含的result，再从里面获取key对应的value
		Set<String> set = queryResultMap.keySet();
		JSONObject parent = null;
		String[] keys = null;
		for (String path : set) {
			if (valuePath.startsWith(path + "/")) {
				try {
					parent = (JSONObject) queryResultMap.get(path);
				} catch (Exception e) {
					Log.e(TAG, "getValueByPath  try { parent = (JSONObject) queryResultMap.get(path); } catch { "
							+ "\n parent not instanceof JSONObject!");
					parent = null;
				}
				if (parent != null) {
					keys = StringUtil.splitPath(valuePath.substring(path.length()));
				}
				break;
			}
		}

		//逐层到达targetKey的直接容器JSONObject parent
		if (keys != null && keys.length > 1) {
			for (int i = 0; i < keys.length - 1; i++) {//一步一步到达指定位置parentPath
				if (parent == null) {//不存在或路径错误(中间的key对应value不是JSONObject)
					break;
				}
				parent = getJSONObject(parent, keys[i]);
			}
		}

		if (parent != null) {
			Log.i(TAG, "getValueByPath >> get from queryResultMap >> return  parent.get(keys[keys.length - 1]);");
			target = parent.get(keys[keys.length - 1]); //值为null应该报错NotExistExeption，一般都是id关联，不可为null，否则可能绕过安全机制
			if (target != null) {
				Log.i(TAG, "getValueByPath >> getValue >> return target = " + target);
				return target;
			}
		}


		//从requestObject中取值
		target = getValue(requestObject, StringUtil.splitPath(valuePath));
		if (target != null) {
			Log.i(TAG, "getValueByPath >> getValue >> return target = " + target);
			return target;
		}

		Log.i(TAG, "getValueByPath  return valuePath;");
		return valuePath;
	}

	//依赖引用关系 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	public static JSONObject getJSONObject(JSONObject object, String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			Log.i(TAG, "getJSONObject  try { return object.getJSONObject(key);"
					+ " } catch (Exception e) { \n"  + e.getMessage());
		}
		return null;
	}


	/**获取数据库返回的String
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public synchronized JSONObject executeSQL(SQLConfig config) throws Exception {
		Log.i(TAG, "executeSQL  config = " + JSON.toJSONString(config));
		if (noVerifyRole == false) {
			if (config.getRole() == null) {
				if (globleRole != null) {
					config.setRole(globleRole);
				} else {
					config.setRole(getVisitor().getId() <= 0 ? RequestRole.UNKNOWN : RequestRole.LOGIN);
				}
			}
			verifier.verify(config);
		}
		return parseCorrectResponse(config.getTable(), sqlExecutor.execute(config));
	}



}
