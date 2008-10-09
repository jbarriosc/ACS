#ifndef _ACSDDSNC_DATAREADER_LISTENER_IMPL
#define _ACSDDSNC_DATAREADER_LISTENER_IMPL

#include <iostream>
#include "DataReaderListener.h"

namespace ddsnc{

/* template arguments
* DRV: <type>DataReader_var class
* DR: <type>DataReader class
* D: <type> struct
*/
template<class DRV, class DR, class D>
class ACSDDSNCDataReaderListener : 
	public ddsnc::DataReaderListenerImpl
{
	public:
	typedef void (*eventHandlerFunction)(D eventData, void *handlerParam);
	eventHandlerFunction templateFunction_mp;

	ACSDDSNCDataReaderListener(eventHandlerFunction templateFunction) : 
		ddsnc::DataReaderListenerImpl()
	{
		templateFunction_mp = templateFunction;
	}

	void on_data_available(DDS::DataReader_ptr reader) 
		throw (CORBA::SystemException)
	{
		num_reads_ ++;
		try {
			DRV message_dr= DR::_narrow(reader);
			if (CORBA::is_nil (message_dr.in ())) {
				::std::cerr << "read: _narrow failed." << ::std::endl;
			}
			D message;
			DDS::SampleInfo si;
			DDS::ReturnCode_t status = message_dr->take_next_sample(message, si);
			if (status == DDS::RETCODE_OK) {
				::std::cerr << "SampleInfo.sample_rank = " << si.sample_rank << ::std::endl;
				::std::cerr << "SampleInfo.instance_state = " << si.instance_state << ::std::endl;
				if (si.valid_data == 1){
					(*templateFunction_mp)(message, 0);
				}else if (si.instance_state == DDS::NOT_ALIVE_DISPOSED_INSTANCE_STATE){
					::std::cerr << "instance is disposed" << ::std::endl;
				}else if (si.instance_state == DDS::NOT_ALIVE_NO_WRITERS_INSTANCE_STATE){
					::std::cerr << "instance is unregistered" << ::std::endl;
				}else {
					ACE_ERROR ((LM_ERROR, 
								"(%P|%t)DataReaderListenerImpl::on_data_available:"
								" received unknown instance state %d\n", 
								si.instance_state));
				}
			}else if (status == DDS::RETCODE_NO_DATA) {
				::std::cerr << "ERROR: reader received DDS::RETCODE_NO_DATA!" << ::std::endl;
			}else {
				::std::cerr << "ERROR: read Message: Error: " <<  status << ::std::endl;
			}
		}catch (CORBA::Exception& e) {
			::std::cerr << "Exception caught in read:" << ::std::endl << e << ::std::endl;
			exit(1);
		}
	}

};
}
#endif
