                                <sec:authorize access="hasAnyRole(${xs:getPermissions('{module}_{modelName}')})">
                                    <li class="${index eq "{module}_{modelName}"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/{module}/{modelName}/index">
                                            <span>{module_name}</span>
                                        </a>
                                    </li>
                                </sec:authorize>