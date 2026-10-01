import React, { createContext, useContext, useState, useEffect } from 'react';
import { projectService } from '../services/projectService';
import { useAuth } from './AuthContext';
import { SAMPLE_PROGRAMS } from '../utils/samplePrograms';

const ProjectContext = createContext(null);

const DEFAULT_PLAYGROUND_FILE = {
  id: 'local-main',
  name: 'main.c',
  language: 'c',
  content: SAMPLE_PROGRAMS[0].code,
  isDirty: false,
};

export const ProjectProvider = ({ children }) => {
  const { isAuthenticated } = useAuth();
  const [projects, setProjects] = useState([]);
  const [activeProject, setActiveProject] = useState(null);
  const [openFiles, setOpenFiles] = useState([DEFAULT_PLAYGROUND_FILE]);
  const [activeFileId, setActiveFileId] = useState(DEFAULT_PLAYGROUND_FILE.id);
  const [loadingProjects, setLoadingProjects] = useState(false);
  const [autosaveEnabled, setAutosaveEnabled] = useState(true);

  // Load user projects on login
  useEffect(() => {
    if (isAuthenticated) {
      loadProjects();
    } else {
      setProjects([]);
      setActiveProject(null);
    }
  }, [isAuthenticated]);

  const loadProjects = async () => {
    try {
      setLoadingProjects(true);
      const data = await projectService.getProjects();
      setProjects(data);
      if (data.length > 0 && !activeProject) {
        selectProject(data[0]);
      }
    } catch (err) {
      console.error('Failed to load projects:', err);
    } finally {
      setLoadingProjects(false);
    }
  };

  const selectProject = (project) => {
    setActiveProject(project);
    if (project.files && project.files.length > 0) {
      setOpenFiles(project.files.map(f => ({ ...f, isDirty: false })));
      setActiveFileId(project.files[0].id);
    } else {
      setOpenFiles([]);
      setActiveFileId(null);
    }
  };

  const createProject = async (name, description) => {
    const newProj = await projectService.createProject({ name, description });
    setProjects(prev => [newProj, ...prev]);
    selectProject(newProj);
    return newProj;
  };

  const deleteProject = async (id) => {
    await projectService.deleteProject(id);
    setProjects(prev => prev.filter(p => p.id !== id));
    if (activeProject && activeProject.id === id) {
      setActiveProject(null);
      setOpenFiles([DEFAULT_PLAYGROUND_FILE]);
      setActiveFileId(DEFAULT_PLAYGROUND_FILE.id);
    }
  };

  const createFile = async (projectId, name, content = '') => {
    if (activeProject) {
      const newFile = await projectService.createFile(projectId, { name, content });
      const fileWithDirty = { ...newFile, isDirty: false };
      setOpenFiles(prev => [...prev, fileWithDirty]);
      setActiveFileId(newFile.id);
      
      // Update active project files
      setActiveProject(prev => ({
        ...prev,
        files: [...(prev.files || []), newFile]
      }));
      return newFile;
    } else {
      const localFile = {
        id: 'local-' + Date.now(),
        name,
        language: 'c',
        content,
        isDirty: false,
      };
      setOpenFiles(prev => [...prev, localFile]);
      setActiveFileId(localFile.id);
      return localFile;
    }
  };

  const deleteFile = async (fileId) => {
    if (activeProject && !String(fileId).startsWith('local-')) {
      await projectService.deleteFile(fileId);
      setActiveProject(prev => ({
        ...prev,
        files: (prev.files || []).filter(f => f.id !== fileId)
      }));
    }
    setOpenFiles(prev => prev.filter(f => f.id !== fileId));
    if (activeFileId === fileId) {
      const remaining = openFiles.filter(f => f.id !== fileId);
      setActiveFileId(remaining.length > 0 ? remaining[0].id : null);
    }
  };

  const updateActiveFileContent = (newContent) => {
    setOpenFiles(prev =>
      prev.map(f =>
        f.id === activeFileId ? { ...f, content: newContent, isDirty: true } : f
      )
    );
  };

  const saveFile = async (fileId = activeFileId) => {
    const file = openFiles.find(f => f.id === fileId);
    if (!file) return;

    if (activeProject && !String(file.id).startsWith('local-')) {
      await projectService.updateFileContent(file.id, file.content);
    }
    setOpenFiles(prev =>
      prev.map(f => (f.id === fileId ? { ...f, isDirty: false } : f))
    );
  };

  const loadSampleProgram = (sample) => {
    const sampleFile = {
      id: 'sample-' + sample.id + '-' + Date.now(),
      name: sample.name.replace(/\s+/g, '_').toLowerCase() + '.c',
      language: 'c',
      content: sample.code,
      isDirty: false,
    };
    setOpenFiles(prev => [...prev, sampleFile]);
    setActiveFileId(sampleFile.id);
  };

  const closeFileTab = (fileId) => {
    const remaining = openFiles.filter(f => f.id !== fileId);
    setOpenFiles(remaining);
    if (activeFileId === fileId) {
      setActiveFileId(remaining.length > 0 ? remaining[remaining.length - 1].id : null);
    }
  };

  const activeFile = openFiles.find(f => f.id === activeFileId) || openFiles[0] || null;

  return (
    <ProjectContext.Provider
      value={{
        projects,
        activeProject,
        openFiles,
        activeFile,
        activeFileId,
        loadingProjects,
        autosaveEnabled,
        setAutosaveEnabled,
        setActiveFileId,
        selectProject,
        createProject,
        deleteProject,
        createFile,
        deleteFile,
        updateActiveFileContent,
        saveFile,
        loadSampleProgram,
        closeFileTab,
        loadProjects,
      }}
    >
      {children}
    </ProjectContext.Provider>
  );
};

export const useProject = () => useContext(ProjectContext);
