/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.andre.maven.install.ui;

/**
 *
 * @author obinna.asuzu
 */
public class Project {
 private String modelVersion;
 private String groupId;
 private String artifactId;
 private String version;
 private String packaging;
 private String name;
 private String description;
 Organization OrganizationObject;


 // Getter Methods 

 public String getModelVersion() {
  return modelVersion;
 }

 public String getGroupId() {
  return groupId;
 }

 public String getArtifactId() {
  return artifactId;
 }

 public String getVersion() {
  return version;
 }

 public String getPackaging() {
  return packaging;
 }

 public String getName() {
  return name;
 }

 public String getDescription() {
  return description;
 }

 public Organization getOrganization() {
  return OrganizationObject;
 }

 // Setter Methods 

 public void setModelVersion(String modelVersion) {
  this.modelVersion = modelVersion;
 }

 public void setGroupId(String groupId) {
  this.groupId = groupId;
 }

 public void setArtifactId(String artifactId) {
  this.artifactId = artifactId;
 }

 public void setVersion(String version) {
  this.version = version;
 }

 public void setPackaging(String packaging) {
  this.packaging = packaging;
 }

 public void setName(String name) {
  this.name = name;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public void setOrganization(Organization organizationObject) {
  this.OrganizationObject = organizationObject;
 }
}
