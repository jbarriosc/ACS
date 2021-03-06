#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# (c) European Southern Observatory, 2002
# Copyright by ESO (in the framework of the ALMA collaboration),
# All rights reserved
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
# MA 02111-1307  US
#
# "@(#) $Id: Makefile,v 1.28 2013/02/26 15:28:54 hsommer Exp $"
#
# Makefile of ........
#
# who       when      what
# --------  --------  ----------------------------------------------
# hsommer  26/11/02  created
#

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

DEBUG=on 
#COMPONENT_HELPERS=on

# 
# IDL Files and flags
# 
IDL_FILES = jconttest_if
IDL_TAO_FLAGS =
USER_IDL =
jconttest_ifStubs_LIBS = acscommonStubs acscomponentStubs ACSErrTypeCommonStubs

#
# Jarfiles and their directories
#
COMPONENTS_JARFILES = jcontTestComp
jcontTestComp_DIRS = \
	alma/jconttest/ContainerServicesTesterImpl \
	alma/jconttest/DummyComponentImpl \
	alma/jconttest/DummyComponent2Impl \
	alma/jconttest/DummyComponentWrapperImpl \
	alma/jconttest/DataReceiverImpl \
	alma/jconttest/ComponentWithBadNullsImpl

JARFILES = jcontTest
jcontTest_DIRS = alma/acs \
	alma/jconttest/util



#
# Scripts (public and local)
# ----------------------------
SCRIPTS         = 
SCRIPTS_L       = \
	jcontTATPrologue \
	selfcontainedUnitTestsJCONT \
	componentUnitTestsJCONT \
	classpathTestsJCONT \
	DynamicContainerConfigurationTest.sh \
	clientTestsJCONT \
	containerJmxMonitorTest


#>>>>> END OF standard rules

#
# INCLUDE STANDARDS
# -----------------
MAKEDIRTMP := $(shell searchFile include/acsMakefile)
ifneq ($(MAKEDIRTMP),\#error\#)
   MAKEDIR := $(MAKEDIRTMP)/include
   include $(MAKEDIR)/acsMakefile
endif


#
# TARGETS
# -------
all:	do_all
	@mkdir -p tmp
	@echo " . . . 'all' done" 

clean : clean_all
	$(RM) *~
	$(RM) *\.log
	$(RM) sed\.scan
	$(RM) tmp
	$(RM) tatlogs
	@echo " . . . clean done"

clean_dist : clean_all clean_dist_all 
	@echo " . . . clean_dist done"

man   : do_man 
	@echo " . . . man page(s) done"

install : install_all
	@echo " . . . installation done"


#___oOo___
