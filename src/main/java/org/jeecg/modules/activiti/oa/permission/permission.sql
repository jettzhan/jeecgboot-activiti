-- 注意：该页面对应的前台目录为views/leave文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值

INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('5190472069456590848', NULL, 'oa请假', '/leave/zhLeaveOaList', 'leave/ZhLeaveOaList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('5624328056313086976', '5190472069456590848', '添加', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('7257851671557971968', '5190472069456590848', '编辑', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('7382699471315492864', '5190472069456590848', '删除', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('3272140948594588672', '5190472069456590848', '批量删除', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('5001247814382637056', '5190472069456590848', '导出excel_', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2776597189593315328', '5190472069456590848', '导入excel_', NULL, NULL, 0, NULL, NULL, 2, 'leave:zhLeaveOa:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-22 06:42:56', NULL, NULL, 0, 0, '1', 0);