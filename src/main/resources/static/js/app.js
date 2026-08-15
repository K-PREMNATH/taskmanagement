/* =========================================================================
   ErconAdjustment Task Tracker - frontend logic
   Plain fetch() against the /api/** REST endpoints. No build step needed;
   this file is served as a static resource by Spring Boot / JBoss.
   ========================================================================= */

const API = '/api';

// In-memory caches, refreshed from the server
let cache = {
  statuses: [],
  priorities: [],
  systems: [],
  employees: [],
  tasks: [],
  assignments: [],
  progress: []
};

let editingTaskId = null;
let editingSystemId = null;
let editingEmployeeId = null;

// ---------------------------------------------------------------------
// Bootstrapping
// ---------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', () => {
  wireNav();
  wireButtons();
  loadEverything();
});

function wireNav() {
  document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => switchView(item.dataset.view));
  });
}

function switchView(name) {
  document.querySelectorAll('.nav-item').forEach(n => n.classList.toggle('active', n.dataset.view === name));
  document.querySelectorAll('.view').forEach(v => v.classList.toggle('hidden', v.id !== `view-${name}`));
}

function wireButtons() {
  document.getElementById('btn-new-task').addEventListener('click', () => openTaskModal());
  document.getElementById('btn-new-assignment').addEventListener('click', () => openAssignmentModal());
  document.getElementById('btn-new-progress').addEventListener('click', () => openProgressModal());
  document.getElementById('btn-new-system').addEventListener('click', () => openSystemModal());
  document.getElementById('btn-new-employee').addEventListener('click', () => openEmployeeModal());

  document.getElementById('task-save').addEventListener('click', saveTask);
  document.getElementById('assign-save').addEventListener('click', saveAssignment);
  document.getElementById('progress-save').addEventListener('click', saveProgress);
  document.getElementById('system-save').addEventListener('click', saveSystem);
  document.getElementById('employee-save').addEventListener('click', saveEmployee);
}

async function loadEverything() {
  try {
    const [statuses, priorities, systems, employees, tasks, assignments, progress] = await Promise.all([
      getJSON('/lookups/statuses'),
      getJSON('/lookups/priorities'),
      getJSON('/systems'),
      getJSON('/employees'),
      getJSON('/tasks'),
      getJSON('/assignments'),
      getJSON('/progress')
    ]);
    cache = { statuses, priorities, systems, employees, tasks, assignments, progress };
    renderAll();
  } catch (err) {
    showToast('Could not load data — is the backend running? (' + err.message + ')', true);
  }
}

function renderAll() {
  renderDashboard();
  renderTasks();
  renderAssignments();
  renderProgress();
  renderSystems();
  renderEmployees();
}

// ---------------------------------------------------------------------
// Fetch helpers
// ---------------------------------------------------------------------

async function getJSON(path) {
  const res = await fetch(API + path);
  if (!res.ok) throw new Error(`${path} -> ${res.status}`);
  return res.json();
}

async function sendJSON(path, method, body) {
  const res = await fetch(API + path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({ message: res.statusText }));
    throw new Error(err.message || 'Request failed');
  }
  return res.status === 204 ? null : res.json();
}

async function del(path) {
  const res = await fetch(API + path, { method: 'DELETE' });
  if (!res.ok) throw new Error('Delete failed');
}

// ---------------------------------------------------------------------
// Shared render helpers
// ---------------------------------------------------------------------

function statusPillClass(statusName) {
  const key = (statusName || '').toLowerCase().replace(/\s+/g, '');
  return `pill pill-${key}`;
}

function fmtDate(d) {
  if (!d) return '—';
  const date = new Date(d + 'T00:00:00');
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}

function fmtDateTime(dt) {
  if (!dt) return '—';
  const date = new Date(dt);
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }) + ' ' +
         date.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' });
}

/* Signature element: a mini "track" bar showing how far a date range has
   progressed against today, colored by the row's current status. */
function renderTrack(startDate, endDate, statusName) {
  if (!startDate || !endDate) return '<span class="mono">—</span>';
  const start = new Date(startDate + 'T00:00:00').getTime();
  const end = new Date(endDate + 'T00:00:00').getTime();
  const now = Date.now();
  let pct = end === start ? 100 : ((now - start) / (end - start)) * 100;
  pct = Math.max(0, Math.min(100, pct));
  const key = (statusName || '').toLowerCase().replace(/\s+/g, '');
  const fillClass = ['blocked', 'completed', 'onhold'].includes(key) ? key : '';
  return `
    <div class="track"><div class="track-fill ${fillClass}" style="width:${pct.toFixed(0)}%"></div></div>
    <div class="track-label">${fmtDate(startDate)} – ${fmtDate(endDate)}</div>
  `;
}

function optionsHTML(items, valueKey, labelFn, selected) {
  return items.map(i => {
    const val = i[valueKey];
    const sel = selected != null && String(selected) === String(val) ? 'selected' : '';
    return `<option value="${val}" ${sel}>${labelFn(i)}</option>`;
  }).join('');
}

function showToast(msg, isError) {
  const el = document.getElementById('toast');
  el.textContent = msg;
  el.classList.toggle('error', !!isError);
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 3200);
}

function openModal(id) { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

// ---------------------------------------------------------------------
// DASHBOARD
// ---------------------------------------------------------------------

function renderDashboard() {
  const totalTasks = cache.tasks.length;
  const inProgress = cache.assignments.filter(a => a.status.statusName === 'In Progress').length;
  const blocked = cache.assignments.filter(a => a.status.statusName === 'Blocked').length;
  const totalManDays = cache.assignments.reduce((sum, a) => sum + Number(a.manDaysAllocated || 0), 0);

  document.getElementById('stat-grid').innerHTML = `
    <div class="stat-card"><div class="label">Total tasks</div><div class="value">${totalTasks}</div></div>
    <div class="stat-card"><div class="label">Assignments in progress</div><div class="value">${inProgress}</div></div>
    <div class="stat-card"><div class="label">Blocked</div><div class="value">${blocked}</div></div>
    <div class="stat-card"><div class="label">Man-days allocated</div><div class="value">${totalManDays.toFixed(1)}</div></div>
  `;

  // Group assignments by employee + status
  const groups = {};
  cache.assignments.forEach(a => {
    const key = a.employee.employeeName + '||' + a.status.statusName;
    if (!groups[key]) {
      groups[key] = {
        employee: a.employee.employeeName,
        status: a.status.statusName,
        count: 0,
        allocated: 0,
        actual: 0
      };
    }
    groups[key].count += 1;
    groups[key].allocated += Number(a.manDaysAllocated || 0);
    groups[key].actual += Number(a.manDaysActual || 0);
  });

  const rows = Object.values(groups).sort((a, b) => a.employee.localeCompare(b.employee));
  const body = document.getElementById('workload-body');
  if (rows.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="5">No assignments yet — add a task and assign it to get started.</td></tr>';
    return;
  }
  body.innerHTML = rows.map(r => `
    <tr>
      <td><strong>${r.employee}</strong></td>
      <td><span class="${statusPillClass(r.status)}">${r.status}</span></td>
      <td class="mono">${r.count}</td>
      <td class="mono">${r.allocated.toFixed(1)}</td>
      <td class="mono">${r.actual.toFixed(1)}</td>
    </tr>
  `).join('');
}

// ---------------------------------------------------------------------
// TASKS
// ---------------------------------------------------------------------

function renderTasks() {
  const body = document.getElementById('tasks-body');
  if (cache.tasks.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="7">No tasks yet. Click "New task" to add one.</td></tr>';
    return;
  }
  body.innerHTML = cache.tasks.map(t => `
    <tr>
      <td><strong>${t.taskName}</strong>${t.description ? `<div class="mono" style="margin-top:2px">${t.description}</div>` : ''}</td>
      <td>${t.system.systemName}</td>
      <td>${t.priority ? t.priority.priorityName : '—'}</td>
      <td><span class="${statusPillClass(t.status.statusName)}">${t.status.statusName}</span></td>
      <td>${renderTrack(t.plannedStartDate, t.plannedEndDate, t.status.statusName)}</td>
      <td class="mono">${fmtDateTime(t.createdDate)}</td>
      <td>
        <button class="btn btn-ghost btn-sm" onclick="openTaskModal(${t.taskId})">Edit</button>
        <button class="btn btn-danger-ghost btn-sm" onclick="removeTask(${t.taskId})">Delete</button>
      </td>
    </tr>
  `).join('');
}

function openTaskModal(taskId) {
  editingTaskId = taskId || null;
  document.getElementById('task-modal-title').textContent = taskId ? 'Edit task' : 'New task';
  document.getElementById('task-system').innerHTML = optionsHTML(cache.systems, 'systemId', s => s.systemName);
  document.getElementById('task-priority').innerHTML =
    '<option value="">None</option>' + optionsHTML(cache.priorities, 'priorityId', p => p.priorityName);
  document.getElementById('task-status').innerHTML = optionsHTML(cache.statuses, 'statusId', s => s.statusName);
  document.getElementById('task-createdby').innerHTML =
    '<option value="">Unassigned</option>' + optionsHTML(cache.employees, 'employeeId', e => e.employeeName);

  if (taskId) {
    const t = cache.tasks.find(x => x.taskId === taskId);
    document.getElementById('task-name').value = t.taskName;
    document.getElementById('task-desc').value = t.description || '';
    document.getElementById('task-system').value = t.system.systemId;
    document.getElementById('task-priority').value = t.priority ? t.priority.priorityId : '';
    document.getElementById('task-status').value = t.status.statusId;
    document.getElementById('task-createdby').value = t.createdBy ? t.createdBy.employeeId : '';
    document.getElementById('task-start').value = t.plannedStartDate || '';
    document.getElementById('task-end').value = t.plannedEndDate || '';
  } else {
    ['task-name', 'task-desc', 'task-start', 'task-end'].forEach(id => document.getElementById(id).value = '');
  }
  openModal('modal-task');
}

async function saveTask() {
  const payload = {
    taskName: document.getElementById('task-name').value.trim(),
    description: document.getElementById('task-desc').value.trim(),
    systemId: Number(document.getElementById('task-system').value),
    priorityId: document.getElementById('task-priority').value || null,
    statusId: Number(document.getElementById('task-status').value),
    createdBy: document.getElementById('task-createdby').value || null,
    plannedStartDate: document.getElementById('task-start').value || null,
    plannedEndDate: document.getElementById('task-end').value || null
  };
  if (!payload.taskName) { showToast('Task name is required', true); return; }

  try {
    if (editingTaskId) {
      await sendJSON(`/tasks/${editingTaskId}`, 'PUT', payload);
      showToast('Task updated');
    } else {
      await sendJSON('/tasks', 'POST', payload);
      showToast('Task created');
    }
    closeModal('modal-task');
    await loadEverything();
  } catch (err) {
    showToast(err.message, true);
  }
}

async function removeTask(id) {
  if (!confirm('Delete this task? Any assignments and progress logs tied to it must be removed first.')) return;
  try {
    await del(`/tasks/${id}`);
    showToast('Task deleted');
    await loadEverything();
  } catch (err) {
    showToast('Could not delete — remove its assignments first.', true);
  }
}

// ---------------------------------------------------------------------
// ASSIGNMENTS
// ---------------------------------------------------------------------

function renderAssignments() {
  const body = document.getElementById('assignments-body');
  if (cache.assignments.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="6">No assignments yet.</td></tr>';
    return;
  }
  body.innerHTML = cache.assignments.map(a => `
    <tr>
      <td><strong>${a.task.taskName}</strong><div class="mono" style="margin-top:2px">${a.task.system.systemName}</div></td>
      <td>${a.employee.employeeName}</td>
      <td>${renderTrack(a.startDate, a.endDate, a.status.statusName)}</td>
      <td class="mono">${Number(a.manDaysAllocated).toFixed(1)} / ${a.manDaysActual != null ? Number(a.manDaysActual).toFixed(1) : '—'}</td>
      <td><span class="${statusPillClass(a.status.statusName)}">${a.status.statusName}</span></td>
      <td><button class="btn btn-danger-ghost btn-sm" onclick="removeAssignment(${a.assignmentId})">Delete</button></td>
    </tr>
  `).join('');
}

function openAssignmentModal() {
  document.getElementById('assign-task').innerHTML =
    optionsHTML(cache.tasks, 'taskId', t => `${t.taskName} (${t.system.systemName})`);
  document.getElementById('assign-employee').innerHTML = optionsHTML(cache.employees, 'employeeId', e => e.employeeName);
  document.getElementById('assign-status').innerHTML = optionsHTML(cache.statuses, 'statusId', s => s.statusName);
  ['assign-start', 'assign-end', 'assign-mandays'].forEach(id => document.getElementById(id).value = '');
  openModal('modal-assignment');
}

async function saveAssignment() {
  const payload = {
    taskId: Number(document.getElementById('assign-task').value),
    employeeId: Number(document.getElementById('assign-employee').value),
    startDate: document.getElementById('assign-start').value,
    endDate: document.getElementById('assign-end').value,
    manDaysAllocated: Number(document.getElementById('assign-mandays').value),
    statusId: Number(document.getElementById('assign-status').value)
  };
  if (!payload.startDate || !payload.endDate || !payload.manDaysAllocated) {
    showToast('Start date, end date, and man-days are required', true);
    return;
  }
  try {
    await sendJSON('/assignments', 'POST', payload);
    showToast('Assignment created');
    closeModal('modal-assignment');
    await loadEverything();
  } catch (err) {
    showToast(err.message, true);
  }
}

async function removeAssignment(id) {
  if (!confirm('Delete this assignment? Its progress log entries must be removed first.')) return;
  try {
    await del(`/assignments/${id}`);
    showToast('Assignment deleted');
    await loadEverything();
  } catch (err) {
    showToast('Could not delete — remove its progress log entries first.', true);
  }
}

// ---------------------------------------------------------------------
// PROGRESS LOG
// ---------------------------------------------------------------------

function renderProgress() {
  const body = document.getElementById('progress-body');
  const rows = [...cache.progress].sort((a, b) => new Date(b.updateDate) - new Date(a.updateDate));
  if (rows.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="6">No progress entries logged yet.</td></tr>';
    return;
  }
  body.innerHTML = rows.map(p => `
    <tr>
      <td><strong>${p.assignment.task.taskName}</strong><div class="mono" style="margin-top:2px">${p.assignment.employee.employeeName}</div></td>
      <td><span class="${statusPillClass(p.status.statusName)}">${p.status.statusName}</span></td>
      <td class="mono">${p.percentComplete}%</td>
      <td>${p.remarks || '—'}</td>
      <td>${p.updatedBy.employeeName}</td>
      <td class="mono">${fmtDateTime(p.updateDate)}</td>
    </tr>
  `).join('');
}

function openProgressModal() {
  document.getElementById('progress-assignment').innerHTML = optionsHTML(
    cache.assignments, 'assignmentId',
    a => `${a.task.taskName} — ${a.employee.employeeName}`
  );
  document.getElementById('progress-status').innerHTML = optionsHTML(cache.statuses, 'statusId', s => s.statusName);
  document.getElementById('progress-updatedby').innerHTML = optionsHTML(cache.employees, 'employeeId', e => e.employeeName);
  document.getElementById('progress-percent').value = '';
  document.getElementById('progress-remarks').value = '';
  openModal('modal-progress');
}

async function saveProgress() {
  const payload = {
    assignmentId: Number(document.getElementById('progress-assignment').value),
    statusId: Number(document.getElementById('progress-status').value),
    percentComplete: Number(document.getElementById('progress-percent').value),
    remarks: document.getElementById('progress-remarks').value.trim(),
    updatedBy: Number(document.getElementById('progress-updatedby').value)
  };
  if (payload.percentComplete === '' || isNaN(payload.percentComplete)) {
    showToast('% complete is required', true);
    return;
  }
  try {
    await sendJSON('/progress', 'POST', payload);
    showToast('Progress logged');
    closeModal('modal-progress');
    await loadEverything();
  } catch (err) {
    showToast(err.message, true);
  }
}

// ---------------------------------------------------------------------
// SYSTEMS
// ---------------------------------------------------------------------

function renderSystems() {
  const body = document.getElementById('systems-body');
  if (cache.systems.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="4">No systems yet.</td></tr>';
    return;
  }
  body.innerHTML = cache.systems.map(s => `
    <tr>
      <td><strong>${s.systemName}</strong></td>
      <td>${s.description || '—'}</td>
      <td><span class="${s.isActive ? 'pill pill-completed' : 'pill pill-cancelled'}">${s.isActive ? 'Active' : 'Inactive'}</span></td>
      <td>
        <button class="btn btn-ghost btn-sm" onclick="openSystemModal(${s.systemId})">Edit</button>
        <button class="btn btn-danger-ghost btn-sm" onclick="removeSystem(${s.systemId})">Delete</button>
      </td>
    </tr>
  `).join('');
}

function openSystemModal(systemId) {
  editingSystemId = systemId || null;
  document.getElementById('system-modal-title').textContent = systemId ? 'Edit system' : 'New system';
  if (systemId) {
    const s = cache.systems.find(x => x.systemId === systemId);
    document.getElementById('system-name').value = s.systemName;
    document.getElementById('system-desc').value = s.description || '';
  } else {
    document.getElementById('system-name').value = '';
    document.getElementById('system-desc').value = '';
  }
  openModal('modal-system');
}

async function saveSystem() {
  const payload = {
    systemName: document.getElementById('system-name').value.trim(),
    description: document.getElementById('system-desc').value.trim(),
    isActive: true
  };
  if (!payload.systemName) { showToast('System name is required', true); return; }
  try {
    if (editingSystemId) {
      await sendJSON(`/systems/${editingSystemId}`, 'PUT', payload);
      showToast('System updated');
    } else {
      await sendJSON('/systems', 'POST', payload);
      showToast('System created');
    }
    closeModal('modal-system');
    await loadEverything();
  } catch (err) {
    showToast(err.message, true);
  }
}

async function removeSystem(id) {
  if (!confirm('Delete this system? Any tasks under it must be removed first.')) return;
  try {
    await del(`/systems/${id}`);
    showToast('System deleted');
    await loadEverything();
  } catch (err) {
    showToast('Could not delete — remove its tasks first.', true);
  }
}

// ---------------------------------------------------------------------
// EMPLOYEES
// ---------------------------------------------------------------------

function renderEmployees() {
  const body = document.getElementById('employees-body');
  if (cache.employees.length === 0) {
    body.innerHTML = '<tr class="empty-row"><td colspan="5">No teammates yet.</td></tr>';
    return;
  }
  body.innerHTML = cache.employees.map(e => `
    <tr>
      <td><strong>${e.employeeName}</strong></td>
      <td>${e.role || '—'}</td>
      <td>${e.email || '—'}</td>
      <td><span class="${e.isActive ? 'pill pill-completed' : 'pill pill-cancelled'}">${e.isActive ? 'Active' : 'Inactive'}</span></td>
      <td>
        <button class="btn btn-ghost btn-sm" onclick="openEmployeeModal(${e.employeeId})">Edit</button>
        <button class="btn btn-danger-ghost btn-sm" onclick="removeEmployee(${e.employeeId})">Delete</button>
      </td>
    </tr>
  `).join('');
}

function openEmployeeModal(employeeId) {
  editingEmployeeId = employeeId || null;
  document.getElementById('employee-modal-title').textContent = employeeId ? 'Edit teammate' : 'Add teammate';
  if (employeeId) {
    const e = cache.employees.find(x => x.employeeId === employeeId);
    document.getElementById('employee-name').value = e.employeeName;
    document.getElementById('employee-role').value = e.role || '';
    document.getElementById('employee-email').value = e.email || '';
  } else {
    document.getElementById('employee-name').value = '';
    document.getElementById('employee-role').value = '';
    document.getElementById('employee-email').value = '';
  }
  openModal('modal-employee');
}

async function saveEmployee() {
  const payload = {
    employeeName: document.getElementById('employee-name').value.trim(),
    role: document.getElementById('employee-role').value.trim(),
    email: document.getElementById('employee-email').value.trim(),
    isActive: true
  };
  if (!payload.employeeName) { showToast('Name is required', true); return; }
  try {
    if (editingEmployeeId) {
      await sendJSON(`/employees/${editingEmployeeId}`, 'PUT', payload);
      showToast('Teammate updated');
    } else {
      await sendJSON('/employees', 'POST', payload);
      showToast('Teammate added');
    }
    closeModal('modal-employee');
    await loadEverything();
  } catch (err) {
    showToast(err.message, true);
  }
}

async function removeEmployee(id) {
  if (!confirm('Delete this teammate? Any assignments tied to them must be removed first.')) return;
  try {
    await del(`/employees/${id}`);
    showToast('Teammate deleted');
    await loadEverything();
  } catch (err) {
    showToast('Could not delete — remove their assignments first.', true);
  }
}
