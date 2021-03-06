/*******************************************************************************
*    ALMA - Atacama Large Millimiter Array
*    (c) European Southern Observatory, 2002
*    Copyright by ESO (in the framework of the ALMA collaboration),
*    All rights reserved
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Lesser General Public
*    License as published by the Free Software Foundation; either
*    version 2.1 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Lesser General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public
*    License along with this library; if not, write to the Free Software
*    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
*
* E.S.O. - ACS project
*
* "@(#) $Id: baciRWfloat.cpp,v 1.4 2008/10/27 14:13:24 bjeram Exp $"
*
* who       when      what
* --------  --------  ----------------------------------------------
* bjeram   2003-02-13 removed all the code
* oat      2003/01/21 added templates for Monitors
* bgustafs 2001-07-12 removed warnings for unused arguments
* msekoran  2001/03/10  modified 
*/

#include "baciRWfloat.h"
#include "baciRWcontImpl_T.i"


#ifdef MAKE_VXWORKS
template class baci::PcommonImpl<ACS_P_T(float, CORBA::Float), POA_ACS::RWfloat>;
template class baci::PcontImpl<ACS_P_T(float, CORBA::Float), POA_ACS::RWfloat>;
template class baci::RWcommonImpl<ACS_RW_T(float, CORBA::Float)>;
#endif
template class baci::RWcontImpl<ACS_RW_T(float, CORBA::Float)>;


/*___oOo___*/

